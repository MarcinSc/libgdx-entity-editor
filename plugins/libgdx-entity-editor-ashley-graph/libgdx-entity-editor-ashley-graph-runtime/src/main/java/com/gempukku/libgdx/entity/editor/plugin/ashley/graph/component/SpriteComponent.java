package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;

public class SpriteComponent extends DirtyComponent {
    private String texturePropertyName = "Texture";
    private float layer;
    private float anchorX = 0.5f;
    private float anchorY = 0.5f;
    private Array<String> tags = new Array<>();
    private String atlas;
    private String texture;
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

    public void addTag(String tag) {
        if (!tags.contains(tag, false)) {
            tags.add(tag);
            setDirty();
        }
    }

    public void removeTag(String tag) {
        if (tags.removeValue(tag, false)) {
            setDirty();
        }
    }

    public void setTexture(String atlas, String texture) {
        this.atlas = atlas;
        this.texture = texture;
        setDirty();
    }

    public String getTexturePropertyName() {
        return texturePropertyName;
    }

    public String getAtlas() {
        return atlas;
    }

    public String getTexture() {
        return texture;
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
