package run.halo.halo.core.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import run.halo.halo.core.entity.Setting;

@ApplicationScoped
public class SettingService {
    
    public static final String IS_INSTALLED = "system.is.installed";
    
    public Uni<Boolean> isInstalled() {
        return Setting.findById(IS_INSTALLED)
                .onItem().ifNull().continueWith(() -> false)
                .onItem().ifNotNull().transform(setting -> {
                    return Boolean.parseBoolean(setting.value);
                });
    }
    
    public Uni<Void> markAsInstalled() {
        Setting setting = new Setting();
        setting.key = IS_INSTALLED;
        setting.value = "true";
        return setting.persist().replaceWithVoid();
    }
}
