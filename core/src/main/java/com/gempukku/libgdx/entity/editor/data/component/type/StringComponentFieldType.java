package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.StringEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class StringComponentFieldType implements ComponentFieldType<String> {
    public static final String ID = "String";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "String";
    }

    @Override
    public Actor createEditor(boolean editable, String fieldValue, Consumer<String> consumer) {
        if (fieldValue == null)
            fieldValue = getDefaultValue();
        return new StringEditorWidget(editable, fieldValue, consumer);
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        String stringType = type.asString();
        return stringType.equals("java.lang.String") || stringType.equals("String");
    }

    @Override
    public JsonValue convertToJson(String value) {
        return new JsonValue(value);
    }

    @Override
    public String getDefaultValue() {
        return "";
    }
}
