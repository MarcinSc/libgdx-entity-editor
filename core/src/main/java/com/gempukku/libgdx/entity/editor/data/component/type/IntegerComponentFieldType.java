package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.IntegerEditorWidget;
import com.github.javaparser.ast.type.Type;

public class IntegerComponentFieldType implements ComponentFieldType<Number> {
    @Override
    public String getId() {
        return "int";
    }

    @Override
    public String getName() {
        return "int";
    }

    @Override
    public Actor createEditor(float labelWidth, boolean editable, String fieldName, Number fieldValue,
                              ObjectMap<String, Object> componentData, Runnable callback) {
        if (fieldValue == null)
            fieldValue = 0;
        return new IntegerEditorWidget(labelWidth, editable, fieldName, fieldValue.intValue(),
                new IntegerEditorWidget.Callback() {
                    @Override
                    public void update(int value) {
                        componentData.put(fieldName, value);
                        callback.run();
                    }
                });
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        return type.isPrimitiveType() && type.asString().equals("int");
    }
}
