package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;


public class FacingComponent extends DirtyComponent {
    private FaceDirection faceDirection = FaceDirection.Right;

    public FaceDirection getFaceDirection() {
        return faceDirection;
    }

    public void setFaceDirection(FaceDirection faceDirection) {
        if (this.faceDirection != faceDirection) {
            this.faceDirection = faceDirection;
            setDirty();
        }
    }
}
