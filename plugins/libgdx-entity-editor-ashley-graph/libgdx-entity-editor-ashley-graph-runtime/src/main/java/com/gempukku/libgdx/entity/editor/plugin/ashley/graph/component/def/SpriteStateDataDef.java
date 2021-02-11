package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def;

import com.badlogic.gdx.utils.ObjectMap;

public class SpriteStateDataDef {
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(ObjectMap<String, Object> properties) {
        this.properties = properties;
    }
}
