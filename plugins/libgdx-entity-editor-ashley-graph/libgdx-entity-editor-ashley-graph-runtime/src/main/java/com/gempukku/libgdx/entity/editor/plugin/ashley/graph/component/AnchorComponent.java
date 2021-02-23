package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.ashley.core.Component;

public class AnchorComponent implements Component {
    private float x = 0.5f;
    private float y = 0.5f;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setAnchor(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
