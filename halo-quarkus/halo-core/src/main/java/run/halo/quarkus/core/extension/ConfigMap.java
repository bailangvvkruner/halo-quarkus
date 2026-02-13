package run.halo.quarkus.core.extension;

import java.util.HashMap;
import java.util.Map;

public class ConfigMap extends Extension {
    
    private static final String API_VERSION = "v1alpha1";
    
    private Map<String, String> data = new HashMap<>();
    
    public ConfigMap() {
        setApiVersion(API_VERSION);
        setKind("ConfigMap");
        setMetadata(new Metadata());
    }
    
    public Map<String, String> getData() {
        return data;
    }
    
    public void setData(Map<String, String> data) {
        this.data = data;
    }
    
    public static class SystemStates {
        public static final String SYSTEM_STATES_CONFIGMAP = "system-states";
        public static final String GROUP = "states";
        
        private Boolean isSetup;
        
        public Boolean getIsSetup() {
            return isSetup;
        }
        
        public void setIsSetup(Boolean isSetup) {
            this.isSetup = isSetup;
        }
    }
}
