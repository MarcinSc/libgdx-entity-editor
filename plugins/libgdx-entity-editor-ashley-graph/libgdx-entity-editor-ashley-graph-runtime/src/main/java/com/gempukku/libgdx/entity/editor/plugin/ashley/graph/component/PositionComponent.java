package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

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

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }
}
