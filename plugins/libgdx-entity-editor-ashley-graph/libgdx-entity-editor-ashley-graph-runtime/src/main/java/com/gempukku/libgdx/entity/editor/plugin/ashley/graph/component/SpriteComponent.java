package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteComponent extends DirtyComponent {
    private float layer = 0f;
    private float width = 1f;
    private float height = 1f;
    private Array<String> tags = new Array<>();
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    public float getLayer() {
        return layer;
    }

    public void setLayer(float layer) {
        if (this.layer != layer) {
            this.layer = layer;
            setDirty();
        }
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        if (this.width != width) {
            this.width = width;
            setDirty();
        }
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        if (this.height != height) {
            this.height = height;
            setDirty();
        }
    }

    public Iterable<String> getTags() {
        return tags;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag, false);
    }

    public void setTags(Array<String> tags) {
        if (!tags.equals(this.tags)) {
            this.tags.clear();
            this.tags.addAll(tags);
            setDirty();
        }
    }

    public void setProperties(ObjectMap<String, Object> properties) {
        this.properties.clear();
        this.properties.putAll(properties);
        setDirty();
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }
}
