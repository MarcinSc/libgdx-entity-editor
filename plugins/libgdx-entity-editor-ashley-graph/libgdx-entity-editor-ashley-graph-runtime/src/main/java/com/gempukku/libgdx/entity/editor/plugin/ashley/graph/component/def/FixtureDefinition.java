package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def;

import com.badlogic.gdx.utils.Array;

public class FixtureDefinition {
    private float friction = 0.2f;
    private float restitution = 0;
    private float density = 0;
    private boolean sensor = false;
    private String sensorName = "";
    private String sensorType = "";
    private Array<String> category = new Array<>();
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

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public Array<String> getCategory() {
        return category;
    }

    public void setCategory(Array<String> category) {
        this.category = category;
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
