package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.LongEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class LongComponentFieldType implements ComponentFieldType<Number> {
    public static final String ID = "long";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "long";
    }

    @Override
    public Actor createEditor(boolean editable, Number fieldValue, Consumer<Number> consumer) {
        if (fieldValue == null)
            fieldValue = getDefaultValue();
        return new LongEditorWidget(editable, fieldValue.intValue(), consumer);
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        String str = type.asString();
        return (type.isPrimitiveType() && str.equals("long"))
                || (str.equals("Long") || str.equals("java.lang.Long"));
    }

    @Override
    public JsonValue convertToJson(Number value) {
        return new JsonValue(value.longValue());
    }

    @Override
    public Number getDefaultValue() {
        return 1L;
    }
}
