package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteComponent implements Component {
    private float layer = 0f;
    private float width = 1f;
    private float height = 1f;
    private Array<String> tags = new Array<>();
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public float getLayer() {
        return layer;
    }

    public void setLayer(float layer) {
        this.layer = layer;
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

    public Iterable<String> getTags() {
        return tags;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag, false);
    }

    public void setTags(Array<String> tags) {
        this.tags.clear();
        this.tags.addAll(tags);
    }

    public void setProperties(ObjectMap<String, Object> properties) {
        this.properties.clear();
        this.properties.putAll(properties);
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }
}
