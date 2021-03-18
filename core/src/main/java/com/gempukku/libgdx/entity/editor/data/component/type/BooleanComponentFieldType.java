package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.BooleanEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class BooleanComponentFieldType implements ComponentFieldType<Boolean> {
    public static final String ID = "boolean";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "boolean";
    }

    @Override
    public Actor createEditor(boolean editable, Boolean value, Consumer<Boolean> consumer) {
        if (value == null) {
            value = getDefaultValue();
            consumer.accept(value);
        }
        return new BooleanEditorWidget(editable, value, consumer);
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        String str = type.asString();
        return (type.isPrimitiveType() && str.equals("boolean"))
                || (str.equals("Boolean") || str.equals("java.lang.Boolean"));
    }

    @Override
    public JsonValue convertToJson(Boolean value) {
        return new JsonValue(value);
    }

    @Override
    public Boolean convertToValue(JsonValue json) {
        return json.asBoolean();
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }

    @Override
    public boolean isEditorSmall() {
        return true;
    }
}
