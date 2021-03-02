package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;

public class ScaleComponentDataStorage extends ComponentDataStorage<ScaleComponent> {
    public ScaleComponentDataStorage(ScaleComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<ScaleComponent> createDataStorage(ScaleComponent component) {
        return new ScaleComponentDataStorage(component);
    }

    @Override
    public Object getValue(String fieldName) {
        if (fieldName.equals("x"))
            return getComponent().getX();
        else if (fieldName.equals("y"))
            return getComponent().getY();
        else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, Object value) {
        if (fieldName.equals("x"))
            getComponent().setX(((Number) value).floatValue());
        else if (fieldName.equals("y"))
            getComponent().setY(((Number) value).floatValue());
        else
            throw new IllegalArgumentException();
    }
}
