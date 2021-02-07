package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.gdx.math.Vector2;

public class ScaleComponent extends DirtyComponent {
    private float x;
    private float y;

    public Vector2 getScale(Vector2 scale) {
        return scale.set(x, y);
    }

    public void setScale(float x, float y) {
        if (this.x != x || this.y != y) {
            this.x = x;
            this.y = y;
            setDirty();
        }
    }
}
