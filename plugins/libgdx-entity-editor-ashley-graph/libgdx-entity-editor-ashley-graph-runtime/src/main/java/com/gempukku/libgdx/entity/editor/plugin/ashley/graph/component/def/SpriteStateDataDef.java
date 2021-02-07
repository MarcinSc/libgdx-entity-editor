package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def;

import com.badlogic.gdx.utils.ObjectMap;

public class SpriteStateDataDef {
    private String atlas;
    private String texture;
    private ObjectMap<String, Object> properties;

    public String getAtlas() {
        return atlas;
    }

    public String getTexture() {
        return texture;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }
}
