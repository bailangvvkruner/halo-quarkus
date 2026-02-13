package run.halo.quarkus.core.infra;

public interface InitializationStateGetter {
    
    Boolean userInitialized();
    
    Boolean dataInitialized();
}
