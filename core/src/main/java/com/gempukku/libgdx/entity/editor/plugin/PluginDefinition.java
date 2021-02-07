package com.gempukku.libgdx.entity.editor.plugin;

public class PluginDefinition {
    public String jarPath;
    public Class<? extends EntityEditorPluginInitializer> pluginClass;
    public String pluginName;
    public String pluginVersion;
    public boolean loaded;
    public boolean canBeRemoved;

    public PluginDefinition(String jarPath, Class<? extends EntityEditorPluginInitializer> pluginClass,
                            String pluginName, String pluginVersion,
                            boolean loaded, boolean canBeRemoved) {
        this.jarPath = jarPath;
        this.pluginClass = pluginClass;
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.loaded = loaded;
        this.canBeRemoved = canBeRemoved;
    }
}
