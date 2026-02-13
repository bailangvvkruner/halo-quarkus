package run.halo.quarkus.setup;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import run.halo.quarkus.core.extension.ConfigMap;
import run.halo.quarkus.core.extension.User;
import run.halo.quarkus.core.infra.ExtensionStore;
import run.halo.quarkus.core.infra.InitializationStateGetter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Path("/system")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SystemSetupEndpoint {
    
    private final InitializationStateGetter initializationStateGetter = new run.halo.quarkus.core.infra.DefaultInitializationStateGetter();
    
    @GET
    @Path("/setup")
    @Produces(MediaType.TEXT_HTML)
    public Response setupPage() {
        if (initializationStateGetter.userInitialized() && initializationStateGetter.dataInitialized()) {
            return Response.status(Response.Status.FOUND)
                    .header("Location", "/console")
                    .build();
        }
        
        return Response.ok()
                .entity(renderSetupPage())
                .build();
    }
    
    @GET
    @Path("/is-installed")
    public Response isInstalled() {
        Map<String, Boolean> result = new HashMap<>();
        result.put("installed", initializationStateGetter.userInitialized());
        return Response.ok(result).build();
    }
    
    @POST
    @Path("/setup")
    public Response setup(SetupRequest request) {
        if (initializationStateGetter.userInitialized() && initializationStateGetter.dataInitialized()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", "System already initialized"))
                    .build();
        }
        
        try {
            doInitialization(request);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
    
    private void doInitialization(SetupRequest request) {
        User superUser = createSuperUser(request);
        ExtensionStore.getInstance().create(superUser);
        
        ExtensionStore.upsetSystemState(states -> {
            states.setIsSetup(true);
        });
        
        initializePresetData(request.getUsername());
    }
    
    private User createSuperUser(SetupRequest request) {
        User user = new User();
        user.getMetadata().setName(request.getUsername());
        user.getMetadata().setCreationTimestamp(Instant.now());
        
        User.UserSpec userSpec = new User.UserSpec();
        userSpec.setUsername(request.getUsername());
        userSpec.setEmail(request.getEmail());
        userSpec.setPassword(hashPassword(request.getPassword()));
        userSpec.setRole("SUPER_ADMIN");
        userSpec.setDisabled(false);
        
        User.Spec spec = new User.Spec();
        spec.setDisplayName(request.getUsername());
        spec.setEmailVerified(true);
        
        user.setUserSpec(userSpec);
        user.setSpec(spec);
        
        return user;
    }
    
    private String hashPassword(String password) {
        return org.mindrot.jbcrypt.BCrypt.hashpw(password, org.mindrot.jbcrypt.BCrypt.gensalt());
    }
    
    private void initializePresetData(String username) {
        String initialData = loadInitialData();
        Properties properties = new Properties();
        properties.setProperty("username", username);
        properties.setProperty("timestamp", Instant.now().toString());
        
        String processedContent = replacePlaceholders(initialData, properties);
        
        System.out.println("Preset data initialized for user: " + username);
    }
    
    private String loadInitialData() {
        return """
                apiVersion: v1alpha1
                kind: ConfigMap
                metadata:
                  name: basic-config
                data:
                  basic: |
                    {"title": "My Blog", "language": "zh-CN", "externalUrl": ""}
                """;
    }
    
    private String replacePlaceholders(String content, Properties properties) {
        for (String key : properties.stringPropertyNames()) {
            content = content.replace("${" + key + "}", properties.getProperty(key));
        }
        return content;
    }
    
    private String renderSetupPage() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Install Halo Quarkus</title>
                    <style>
                        body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; padding: 20px; }
                        h1 { color: #333; }
                        .form-group { margin-bottom: 20px; }
                        label { display: block; margin-bottom: 5px; font-weight: bold; }
                        input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
                        button { background-color: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }
                        button:hover { background-color: #0056b3; }
                    </style>
                </head>
                <body>
                    <h1>Install Halo Quarkus</h1>
                    <form id="setupForm">
                        <div class="form-group">
                            <label for="username">Username</label>
                            <input type="text" id="username" name="username" required minlength="4" maxlength="63">
                        </div>
                        <div class="form-group">
                            <label for="password">Password</label>
                            <input type="password" id="password" name="password" required minlength="5" maxlength="257">
                        </div>
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" required>
                        </div>
                        <div class="form-group">
                            <label for="siteTitle">Site Title</label>
                            <input type="text" id="siteTitle" name="siteTitle" required maxlength="80">
                        </div>
                        <button type="submit">Install</button>
                    </form>
                    <script>
                        document.getElementById('setupForm').addEventListener('submit', function(e) {
                            e.preventDefault();
                            const data = {
                                username: document.getElementById('username').value,
                                password: document.getElementById('password').value,
                                email: document.getElementById('email').value,
                                siteTitle: document.getElementById('siteTitle').value
                            };
                            fetch('/system/setup', {
                                method: 'POST',
                                headers: {'Content-Type': 'application/json'},
                                body: JSON.stringify(data)
                            }).then(response => {
                                if (response.ok) {
                                    window.location.href = '/console';
                                } else {
                                    alert('Installation failed');
                                }
                            });
                        });
                    </script>
                </body>
                </html>
                """;
    }
}
