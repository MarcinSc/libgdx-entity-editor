package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.LongEditorWidget;
import com.github.javaparser.ast.type.Type;

public class LongComponentFieldType implements ComponentFieldType<Number> {
    @Override
    public String getId() {
        return "long";
    }

    @Override
    public String getName() {
        return "long";
    }

    @Override
    public Actor createEditor(float labelWidth, boolean editable, String fieldName, Number fieldValue,
                              ObjectMap<String, Object> componentData, Runnable callback) {
        if (fieldValue == null)
            fieldValue = 0;
        return new LongEditorWidget(labelWidth, editable, fieldName, fieldValue.intValue(),
                new LongEditorWidget.Callback() {
                    @Override
                    public void update(long value) {
                        componentData.put(fieldName, value);
                        callback.run();
                    }
                });
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        return type.isPrimitiveType() && type.asString().equals("long");
    }
}
