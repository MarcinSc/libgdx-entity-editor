package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

public class PositionComponent extends DirtyComponent {
    private float x;
    private float y;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        if (this.x != x || this.y != y) {
            this.x = x;
            this.y = y;
            setDirty();
        }
    }
}
