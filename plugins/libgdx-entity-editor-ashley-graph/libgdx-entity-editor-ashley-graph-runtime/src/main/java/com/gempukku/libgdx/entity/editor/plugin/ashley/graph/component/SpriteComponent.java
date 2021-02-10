package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;

public class SpriteComponent extends DirtyComponent {
    private float layer;
    private float anchorX = 0.5f;
    private float anchorY = 0.5f;
    private Array<String> tags = new Array<>();
    private ObjectMap<String, Object> properties = new ObjectMap<>();

    private transient GraphSprite graphSprite;

    public GraphSprite getGraphSprite() {
        return graphSprite;
    }

    public void setGraphSprite(GraphSprite graphSprite) {
        this.graphSprite = graphSprite;
    }

    public float getLayer() {
        return layer;
    }

    public void setLayer(float layer) {
        if (this.layer != layer) {
            this.layer = layer;
            setDirty();
        }
    }

    public float getAnchorX() {
        return anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    public void setAnchor(float x, float y) {
        if (anchorX != x || anchorY != y) {
            anchorX = x;
            anchorY = y;
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
