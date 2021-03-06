package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.GraphSpriteProperties;

public class SpriteComponent implements Component {
    private float layer = 0f;
    private float width = 1f;
    private float height = 1f;
    private Array<String> tags = new Array<>();
    private GraphSpriteProperties properties = new GraphSpriteProperties();

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

    public Array<String> getTags() {
        return tags;
    }

    public boolean hasTag(String tag) {
        return tags.contains(tag, false);
    }

    public void setTags(Array<String> tags) {
        this.tags = tags;
    }

    public GraphSpriteProperties getProperties() {
        return properties;
    }

    public void setProperties(GraphSpriteProperties properties) {
        this.properties = properties;
    }
}
