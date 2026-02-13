package run.halo.halo.core.theme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.qute.Template;
import io.qute.TemplateInstance;
import org.jboss.logging.Logger;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@ApplicationScoped
public class ThemeEngine {
    
    private static final Logger LOG = Logger.getLogger(ThemeEngine.class);
    
    @Inject
    io.qute.Engine quteEngine;
    
    @ConfigProperty(name = "halo.theme.location")
    String themeLocation;
    
    private String activeTheme = "default";
    private final Map<String, Template> templateCache = new HashMap<>();
    
    public TemplateInstance render(String templateName, Map<String, Object> data) {
        String fullTemplateName = activeTheme + "/" + templateName;
        Template template = templateCache.computeIfAbsent(fullTemplateName, this::loadTemplate);
        
        TemplateInstance instance = template.instance();
        if (data != null) {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                instance.data(entry.getKey(), entry.getValue());
            }
        }
        return instance;
    }
    
    private Template loadTemplate(String templateName) {
        try {
            Path themePath = Paths.get(themeLocation, activeTheme);
            Path templatePath = themePath.resolve(templateName + ".html");
            
            if (!Files.exists(templatePath)) {
                LOG.warn("Template not found: " + templatePath);
                return quteEngine.getTemplate("fallback");
            }
            
            return quteEngine.getTemplate(templatePath.toString());
        } catch (Exception e) {
            LOG.error("Failed to load template: " + templateName, e);
            return quteEngine.getTemplate("error");
        }
    }
    
    public void setActiveTheme(String themeName) {
        this.activeTheme = themeName;
        templateCache.clear();
        LOG.info("Active theme changed to: " + themeName);
    }
    
    public String getActiveTheme() {
        return activeTheme;
    }
    
    public List<String> listThemes() {
        try {
            Path themesPath = Paths.get(themeLocation);
            if (!Files.exists(themesPath)) {
                Files.createDirectories(themesPath);
                return Collections.emptyList();
            }
            
            List<String> themes = new ArrayList<>();
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(themesPath)) {
                for (Path path : stream) {
                    if (Files.isDirectory(path)) {
                        themes.add(path.getFileName().toString());
                    }
                }
            }
            return themes;
        } catch (IOException e) {
            LOG.error("Failed to list themes", e);
            return Collections.emptyList();
        }
    }
}
