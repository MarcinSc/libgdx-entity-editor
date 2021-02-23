package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;


import com.badlogic.ashley.core.Component;

public class FacingComponent implements Component {
    private FaceDirection faceDirection = FaceDirection.Right;

    public FaceDirection getFaceDirection() {
        return faceDirection;
    }

    public void setFaceDirection(FaceDirection faceDirection) {
        this.faceDirection = faceDirection;
    }
}
