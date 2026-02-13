package run.halo.quarkus.core.extension;

public class User extends Extension {
    
    private static final String API_VERSION = "v1alpha1";
    
    public static final String HIDDEN_USER_LABEL = "halo.run/hidden-user";
    
    private Spec spec;
    private UserSpec userSpec;
    
    public User() {
        setApiVersion(API_VERSION);
        setKind("User");
        setMetadata(new Metadata());
    }
    
    public Spec getSpec() {
        return spec;
    }
    
    public void setSpec(Spec spec) {
        this.spec = spec;
    }
    
    public UserSpec getUserSpec() {
        return userSpec;
    }
    
    public void setUserSpec(UserSpec userSpec) {
        this.userSpec = userSpec;
    }
    
    public static class Spec {
        private String displayName;
        private String avatar;
        private String bio;
        private Boolean emailVerified;
        
        public String getDisplayName() {
            return displayName;
        }
        
        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }
        
        public String getAvatar() {
            return avatar;
        }
        
        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }
        
        public String getBio() {
            return bio;
        }
        
        public void setBio(String bio) {
            this.bio = bio;
        }
        
        public Boolean getEmailVerified() {
            return emailVerified;
        }
        
        public void setEmailVerified(Boolean emailVerified) {
            this.emailVerified = emailVerified;
        }
    }
    
    public static class UserSpec {
        private String username;
        private String email;
        private String password;
        private String role;
        private Boolean disabled;
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public String getPassword() {
            return password;
        }
        
        public void setPassword(String password) {
            this.password = password;
        }
        
        public String getRole() {
            return role;
        }
        
        public void setRole(String role) {
            this.role = role;
        }
        
        public Boolean getDisabled() {
            return disabled;
        }
        
        public void setDisabled(Boolean disabled) {
            this.disabled = disabled;
        }
    }
}
