package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.graph.plugin.sprites.GraphSprite;

public class SpriteComponent extends DirtyComponent {
    private String texturePropertyName = "Texture";
    private float layer;
    private Vector2 anchor;
    private Array<String> tags;
    private String atlas;
    private String texture;
    private ObjectMap<String, Object> properties;

    private transient GraphSprite graphSprite;
    private transient ObjectSet<String> addedTags = new ObjectSet<>();
    private transient ObjectSet<String> removedTags = new ObjectSet<>();

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

    public Vector2 getAnchor() {
        return anchor;
    }

    public Iterable<String> getTags() {
        return tags;
    }

    public Iterable<String> getAddedTags() {
        return addedTags;
    }

    public Iterable<String> getRemovedTags() {
        return removedTags;
    }

    public void addTag(String tag) {
        if (!tags.contains(tag, false)) {
            tags.add(tag);
            addedTags.add(tag);
            removedTags.remove(tag);
            setDirty();
        }
    }

    public void removeTag(String tag) {
        if (tags.removeValue(tag, false)) {
            removedTags.add(tag);
            addedTags.remove(tag);
            setDirty();
        }
    }

    @Override
    public void clean() {
        super.clean();
        addedTags.clear();
        removedTags.clear();
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
