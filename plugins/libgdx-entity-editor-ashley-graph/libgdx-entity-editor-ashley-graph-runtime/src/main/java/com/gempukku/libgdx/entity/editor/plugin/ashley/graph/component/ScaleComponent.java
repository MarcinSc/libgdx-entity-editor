package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.ashley.core.Component;

public class ScaleComponent implements Component {
    private float x = 1;
    private float y = 1;

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
