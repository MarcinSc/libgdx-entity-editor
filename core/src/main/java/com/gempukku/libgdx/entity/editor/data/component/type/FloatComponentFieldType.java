package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.FloatEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class FloatComponentFieldType implements ComponentFieldType<Number> {
    public static final String ID = "float";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "float";
    }

    @Override
    public Actor createEditor(boolean editable, Number value, Consumer<Number> consumer) {
        if (value == null) {
            value = getDefaultValue();
            consumer.accept(value);
        }
        return new FloatEditorWidget(editable, value.floatValue(), consumer);
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        String str = type.asString();
        return (type.isPrimitiveType() && str.equals("float"))
                || (str.equals("Float") || str.equals("java.lang.Float"));
    }

    @Override
    public JsonValue convertToJson(Number value) {
        return new JsonValue(value.floatValue());
    }

    @Override
    public Number convertToValue(JsonValue json) {
        if (json.isNull())
            return getDefaultValue();
        return json.asFloat();
    }

    @Override
    public Number getDefaultValue() {
        return 0f;
    }

    @Override
    public boolean isEditorSmall() {
        return true;
    }
}
