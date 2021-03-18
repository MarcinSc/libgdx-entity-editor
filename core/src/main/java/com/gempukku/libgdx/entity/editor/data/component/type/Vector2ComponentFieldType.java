package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.PairOfFloatsEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class Vector2ComponentFieldType implements ComponentFieldType<Vector2> {
    public static final String ID = "Vector2";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Vector2";
    }

    @Override
    public Actor createEditor(boolean editable, Vector2 value, Consumer<Vector2> consumer) {
        if (value == null) {
            value = getDefaultValue();
            consumer.accept(value);
        }
        return new PairOfFloatsEditorWidget(20, editable, "x", value.x, "y", value.y,
                new PairOfFloatsEditorWidget.Callback() {
                    @Override
                    public void update(float value1, float value2) {
                        consumer.accept(new Vector2(value1, value2));
                    }
                });
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        String stringType = type.asString();
        return stringType.equals("com.badlogic.gdx.math.Vector2") || stringType.equals("Vector2");
    }

    @Override
    public JsonValue convertToJson(Vector2 value) {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        result.addChild("x", new JsonValue(value.x));
        result.addChild("y", new JsonValue(value.y));
        return result;
    }

    @Override
    public Vector2 convertToValue(JsonValue json) {
        return new Vector2(json.getFloat("x"), json.getFloat("y"));
    }

    @Override
    public Vector2 getDefaultValue() {
        return new Vector2();
    }

    @Override
    public boolean isEditorSmall() {
        return true;
    }
}
