package run.halo.quarkus.core.infra;

import run.halo.quarkus.core.extension.ConfigMap;
import run.halo.quarkus.core.extension.User;

import java.util.concurrent.atomic.AtomicBoolean;

public class DefaultInitializationStateGetter implements InitializationStateGetter {
    
    private final AtomicBoolean userInitialized = new AtomicBoolean(false);
    private final AtomicBoolean dataInitialized = new AtomicBoolean(false);
    
    @Override
    public Boolean userInitialized() {
        if (userInitialized.get()) {
            return true;
        }
        boolean hasUser = hasUser();
        userInitialized.set(hasUser);
        return hasUser;
    }
    
    @Override
    public Boolean dataInitialized() {
        if (dataInitialized.get()) {
            return true;
        }
        boolean isSetup = isSetup();
        dataInitialized.set(isSetup);
        return isSetup;
    }
    
    private boolean hasUser() {
        return ExtensionStore.getInstance().list(User.class).stream()
                .filter(u -> !isHiddenUser(u))
                .findFirst()
                .isPresent();
    }
    
    private boolean isHiddenUser(User user) {
        return user.getMetadata() != null
                && user.getMetadata().getLabels() != null
                && "true".equals(user.getMetadata().getLabels().get(User.HIDDEN_USER_LABEL));
    }
    
    private boolean isSetup() {
        ConfigMap configMap = ExtensionStore.getInstance().fetch(
                ConfigMap.class, 
                ConfigMap.SystemStates.SYSTEM_STATES_CONFIGMAP
        );
        if (configMap == null || configMap.getData() == null) {
            return false;
        }
        
        String statesJson = configMap.getData().getOrDefault(ConfigMap.SystemStates.GROUP, "{}");
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            ConfigMap.SystemStates states = mapper.readValue(statesJson, ConfigMap.SystemStates.class);
            return Boolean.TRUE.equals(states.getIsSetup());
        } catch (Exception e) {
            return false;
        }
    }
}
