package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.BooleanEditorWidget;
import com.github.javaparser.ast.type.Type;

public class BooleanComponentFieldType implements ComponentFieldType<Boolean> {
    @Override
    public String getId() {
        return "boolean";
    }

    @Override
    public String getName() {
        return "boolean";
    }

    @Override
    public Actor createEditor(float labelWidth, boolean editable, String fieldName, Boolean fieldValue,
                              ObjectMap<String, Object> componentData, Runnable callback) {
        if (fieldValue == null)
            fieldValue = false;
        return new BooleanEditorWidget(labelWidth, editable, fieldName, fieldValue,
                new BooleanEditorWidget.Callback() {
                    @Override
                    public void update(boolean value) {
                        componentData.put(fieldName, value);
                        callback.run();
                    }
                });
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        return type.isPrimitiveType() && type.asString().equals("boolean");
    }
}
