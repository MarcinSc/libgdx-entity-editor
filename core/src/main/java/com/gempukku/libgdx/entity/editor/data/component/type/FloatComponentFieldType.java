package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.FloatEditorWidget;
import com.github.javaparser.ast.type.Type;

public class FloatComponentFieldType implements ComponentFieldType<Number> {
    @Override
    public String getId() {
        return "float";
    }

    @Override
    public String getName() {
        return "float";
    }

    @Override
    public Actor createEditor(float labelWidth, boolean editable, String fieldName, Number fieldValue,
                              ObjectMap<String, Object> componentData, Runnable callback) {
        if (fieldValue == null)
            fieldValue = 0f;
        return new FloatEditorWidget(labelWidth, editable, fieldName, fieldValue.floatValue(),
                new FloatEditorWidget.Callback() {
                    @Override
                    public void update(float value) {
                        componentData.put(fieldName, value);
                        callback.run();
                    }
                });
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        return type.isPrimitiveType() && type.asString().equals("float");
    }
}
