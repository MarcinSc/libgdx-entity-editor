package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.StringEditorWidget;
import com.github.javaparser.ast.type.Type;

public class StringComponentFieldType implements ComponentFieldType<String> {
    @Override
    public String getId() {
        return "String";
    }

    @Override
    public String getName() {
        return "String";
    }

    @Override
    public Actor createEditor(float labelWidth, boolean editable, String fieldName, String fieldValue,
                              ObjectMap<String, Object> componentData, Runnable callback) {
        if (fieldValue == null)
            fieldValue = "";
        return new StringEditorWidget(labelWidth, editable, fieldName, fieldValue,
                new StringEditorWidget.Callback() {
                    @Override
                    public void update(String value) {
                        componentData.put(fieldName, value);
                        callback.run();
                    }
                });
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        String stringType = type.asString();
        return stringType.equals("java.lang.String") || stringType.equals("String");
    }
}
