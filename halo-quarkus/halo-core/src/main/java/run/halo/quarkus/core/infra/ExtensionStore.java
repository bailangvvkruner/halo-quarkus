package run.halo.quarkus.core.infra;

import run.halo.quarkus.core.extension.ConfigMap;
import run.halo.quarkus.core.extension.Extension;

import java.util.function.Consumer;

public class ExtensionStore {
    
    private static final ExtensionStore INSTANCE = new ExtensionStore();
    
    private final java.util.concurrent.ConcurrentHashMap<String, Extension> store = new java.util.concurrent.ConcurrentHashMap<>();
    
    private ExtensionStore() {}
    
    public static ExtensionStore getInstance() {
        return INSTANCE;
    }
    
    public <T extends Extension> T fetch(Class<T> type, String name) {
        String key = type.getSimpleName() + ":" + name;
        return type.cast(store.get(key));
    }
    
    public <T extends Extension> T create(T extension) {
        String key = extension.getClass().getSimpleName() + ":" + extension.getMetadata().getName();
        store.put(key, extension);
        return extension;
    }
    
    public <T extends Extension> T update(T extension) {
        String key = extension.getClass().getSimpleName() + ":" + extension.getMetadata().getName();
        store.put(key, extension);
        return extension;
    }
    
    public <T extends Extension> void delete(Class<T> type, String name) {
        String key = type.getSimpleName() + ":" + name;
        store.remove(key);
    }
    
    public <T extends Extension> java.util.List<T> list(Class<T> type) {
        String prefix = type.getSimpleName() + ":";
        return store.entrySet().stream()
                .filter(e -> e.getKey().startsWith(prefix))
                .map(e -> type.cast(e.getValue()))
                .toList();
    }
    
    public static void upsetSystemState(Consumer<ConfigMap.SystemStates> consumer) {
        ConfigMap configMap = ExtensionStore.getInstance().fetch(
                ConfigMap.class,
                ConfigMap.SystemStates.SYSTEM_STATES_CONFIGMAP
        );
        
        if (configMap == null) {
            configMap = new ConfigMap();
            configMap.getMetadata().setName(ConfigMap.SystemStates.SYSTEM_STATES_CONFIGMAP);
            ExtensionStore.getInstance().create(configMap);
        }
        
        ConfigMap.SystemStates states = new ConfigMap.SystemStates();
        if (configMap.getData() != null && configMap.getData().containsKey(ConfigMap.SystemStates.GROUP)) {
            try {
                com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                states = mapper.readValue(
                        configMap.getData().get(ConfigMap.SystemStates.GROUP),
                        ConfigMap.SystemStates.class
                );
            } catch (Exception e) {
            }
        }
        
        consumer.accept(states);
        
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String json = mapper.writeValueAsString(states);
            configMap.setData(new java.util.HashMap<>());
            configMap.getData().put(ConfigMap.SystemStates.GROUP, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        ExtensionStore.getInstance().update(configMap);
    }
}
