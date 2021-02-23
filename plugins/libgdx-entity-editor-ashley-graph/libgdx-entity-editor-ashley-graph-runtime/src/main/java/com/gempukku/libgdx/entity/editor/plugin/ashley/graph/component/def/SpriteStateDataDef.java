package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def;

import com.badlogic.gdx.utils.ObjectMap;

public class SpriteStateDataDef {
    private float width = 1f;
    private float height = 1f;
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(ObjectMap<String, Object> properties) {
        this.properties = properties;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
