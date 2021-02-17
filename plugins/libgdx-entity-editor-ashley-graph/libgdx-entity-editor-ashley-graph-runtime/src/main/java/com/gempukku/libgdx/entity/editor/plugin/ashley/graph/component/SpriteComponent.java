package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SpriteComponent extends DirtyComponent {
    private float layer;
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
