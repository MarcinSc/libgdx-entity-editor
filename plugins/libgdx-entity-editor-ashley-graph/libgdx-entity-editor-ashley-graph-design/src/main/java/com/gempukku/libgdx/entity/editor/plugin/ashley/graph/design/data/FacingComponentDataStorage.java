package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FacingComponent;

public class FacingComponentDataStorage extends ComponentDataStorage<FacingComponent> {
    public FacingComponentDataStorage(FacingComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<FacingComponent> createDataStorage(FacingComponent component) {
        return new FacingComponentDataStorage(component);
    }

    @Override
    public JsonValue getValue(String fieldName) {
        if (fieldName.equals("faceDirection"))
            return new JsonValue(getComponent().getFaceDirection().name());
        else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, JsonValue value) {
        if (fieldName.equals("faceDirection"))
            getComponent().setFaceDirection(FaceDirection.valueOf(value.asString()));
        else
            throw new IllegalArgumentException();
    }
}
