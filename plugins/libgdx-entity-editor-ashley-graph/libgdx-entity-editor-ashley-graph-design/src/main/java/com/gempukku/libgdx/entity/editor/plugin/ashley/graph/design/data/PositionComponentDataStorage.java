package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;

public class PositionComponentDataStorage extends ComponentDataStorage<PositionComponent> {
    public PositionComponentDataStorage(PositionComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<PositionComponent> createDataStorage(PositionComponent component) {
        return new PositionComponentDataStorage(component);
    }

    @Override
    public JsonValue getValue(String fieldName) {
        if (fieldName.equals("x"))
            return new JsonValue(getComponent().getX());
        else if (fieldName.equals("y"))
            return new JsonValue(getComponent().getY());
        else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, JsonValue value) {
        if (fieldName.equals("x"))
            getComponent().setX(value.asFloat());
        else if (fieldName.equals("y"))
            getComponent().setY(value.asFloat());
        else
            throw new IllegalArgumentException();
    }
}
