package com.gempukku.example.component;

import com.badlogic.ashley.core.Component;

public class PositionComponent implements Component {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
