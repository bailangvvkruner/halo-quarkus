package run.halo.halo.core.plugin;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.jboss.logging.Logger;
import run.halo.halo.core.entity.Post;

import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class PluginManager {
    
    private static final Logger LOG = Logger.getLogger(PluginManager.class);
    
    private final Map<String, Plugin> plugins = new ConcurrentHashMap<>();
    
    public void onStart(@Observes StartupEvent event) {
        LOG.info("Plugin Manager starting...");
    }
    
    public void loadPlugin(Path pluginPath) {
        try {
            String pluginId = pluginPath.getFileName().toString();
            Plugin plugin = new Plugin(pluginId, pluginPath);
            plugins.put(pluginId, plugin);
            LOG.info("Plugin loaded: " + pluginId);
        } catch (Exception e) {
            LOG.error("Failed to load plugin: " + pluginPath, e);
        }
    }
    
    public void unloadPlugin(String pluginId) {
        Plugin plugin = plugins.remove(pluginId);
        if (plugin != null) {
            plugin.unload();
            LOG.info("Plugin unloaded: " + pluginId);
        }
    }
    
    public List<Plugin> listPlugins() {
        return new ArrayList<>(plugins.values());
    }
    
    public Optional<Plugin> getPlugin(String pluginId) {
        return Optional.ofNullable(plugins.get(pluginId));
    }
    
    public void executeHook(String hookName, Object... args) {
        for (Plugin plugin : plugins.values()) {
            try {
                plugin.executeHook(hookName, args);
            } catch (Exception e) {
                LOG.error("Hook execution failed: " + hookName + " in plugin " + plugin.id, e);
            }
        }
    }
    
    public static class StartupEvent {
    }
    
    public static class Plugin {
        private final String id;
        private final Path path;
        private final Map<String, Object> context;
        
        public Plugin(String id, Path path) {
            this.id = id;
            this.path = path;
            this.context = new HashMap<>();
        }
        
        public void executeHook(String hookName, Object... args) {
            LOG.info("Executing hook: " + hookName + " for plugin: " + id);
        }
        
        public void unload() {
            LOG.info("Unloading plugin: " + id);
        }
        
        public String getId() {
            return id;
        }
        
        public Path getPath() {
            return path;
        }
    }
}
