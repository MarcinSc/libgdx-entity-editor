package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def;

import com.badlogic.gdx.math.Vector2;

public class BoxFixtureShape implements FixtureShape {
    private float width = 1;
    private float height = 1;
    private Vector2 center = new Vector2();
    private float angle = 0;

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

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    @Override
    public String getType() {
        return "Box";
    }
}
