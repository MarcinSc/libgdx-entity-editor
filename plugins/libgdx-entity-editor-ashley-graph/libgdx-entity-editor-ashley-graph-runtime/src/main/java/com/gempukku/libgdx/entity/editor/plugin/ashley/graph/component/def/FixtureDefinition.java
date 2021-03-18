package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def;

import com.badlogic.gdx.utils.Array;

public class FixtureDefinition {
    private float friction = 0.2f;
    private float restitution = 0;
    private float density = 0;
    private boolean sensor = false;
    private String type = "";
    private Array<String> mask = new Array<>();
    private FixtureShape shape = new BoxFixtureShape();

    public float getFriction() {
        return friction;
    }

    public void setFriction(float friction) {
        this.friction = friction;
    }

    public float getRestitution() {
        return restitution;
    }

    public void setRestitution(float restitution) {
        this.restitution = restitution;
    }

    public float getDensity() {
        return density;
    }

    public void setDensity(float density) {
        this.density = density;
    }

    public boolean isSensor() {
        return sensor;
    }

    public void setSensor(boolean sensor) {
        this.sensor = sensor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Array<String> getMask() {
        return mask;
    }

    public void setMask(Array<String> mask) {
        this.mask = mask;
    }

    public FixtureShape getShape() {
        return shape;
    }

    public void setShape(FixtureShape shape) {
        this.shape = shape;
    }
}
