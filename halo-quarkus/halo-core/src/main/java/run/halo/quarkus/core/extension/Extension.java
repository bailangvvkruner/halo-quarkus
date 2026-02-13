package run.halo.quarkus.core.extension;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public abstract class Extension {
    
    private String apiVersion;
    private String kind;
    private Metadata metadata;
    
    public String getApiVersion() {
        return apiVersion;
    }
    
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
    
    public String getKind() {
        return kind;
    }
    
    public void setKind(String kind) {
        this.kind = kind;
    }
    
    public Metadata getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
    
    public static class Metadata {
        private String name;
        private String namespace;
        private Map<String, String> labels = new HashMap<>();
        private Map<String, String> annotations = new HashMap<>();
        private Instant creationTimestamp;
        private Instant deletionTimestamp;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        
        public String getNamespace() {
            return namespace;
        }
        
        public void setNamespace(String namespace) {
            this.namespace = namespace;
        }
        
        public Map<String, String> getLabels() {
            return labels;
        }
        
        public void setLabels(Map<String, String> labels) {
            this.labels = labels;
        }
        
        public Map<String, String> getAnnotations() {
            return annotations;
        }
        
        public void setAnnotations(Map<String, String> annotations) {
            this.annotations = annotations;
        }
        
        public Instant getCreationTimestamp() {
            return creationTimestamp;
        }
        
        public void setCreationTimestamp(Instant creationTimestamp) {
            this.creationTimestamp = creationTimestamp;
        }
        
        public Instant getDeletionTimestamp() {
            return deletionTimestamp;
        }
        
        public void setDeletionTimestamp(Instant deletionTimestamp) {
            this.deletionTimestamp = deletionTimestamp;
        }
    }
}
