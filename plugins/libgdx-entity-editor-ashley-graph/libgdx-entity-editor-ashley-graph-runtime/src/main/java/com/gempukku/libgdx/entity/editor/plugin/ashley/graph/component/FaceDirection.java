package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

public enum FaceDirection {
    Left(-1), Right(1);

    private float direction;

    FaceDirection(int direction) {
        this.direction = direction;
    }

    public float getDirection() {
        return direction;
    }

    public FaceDirection inverse() {
        switch (this) {
            case Left:
                return Right;
            case Right:
                return Left;
        }
        throw new IllegalStateException("Unknown type of direction");
    }
}
