package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def;

public class SpriteStateDataDef {
    private float width = 1f;
    private float height = 1f;
    private GraphSpriteProperties properties = new GraphSpriteProperties();

    public GraphSpriteProperties getProperties() {
        return properties;
    }

    public void setProperties(GraphSpriteProperties properties) {
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
