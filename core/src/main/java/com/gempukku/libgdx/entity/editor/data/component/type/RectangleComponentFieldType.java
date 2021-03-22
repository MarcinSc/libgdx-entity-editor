package com.gempukku.libgdx.entity.editor.data.component.type;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.PairOfFloatsEditorWidget;
import com.github.javaparser.ast.type.Type;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.function.Consumer;

public class RectangleComponentFieldType implements ComponentFieldType<Rectangle> {
    public static final String ID = "Rectangle";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Rectangle";
    }

    @Override
    public Actor createEditor(boolean editable, Rectangle value, Consumer<Rectangle> consumer) {
        if (value == null) {
            value = getDefaultValue();
            consumer.accept(value);
        }
        VisTable table = new VisTable();

        Rectangle finalValue = value;

        PairOfFloatsEditorWidget positionWidget = new PairOfFloatsEditorWidget(40, editable, "x", value.x, "y", value.y,
                new PairOfFloatsEditorWidget.Callback() {
                    @Override
                    public void update(float value1, float value2) {
                        finalValue.x = value1;
                        finalValue.y = value2;
                        consumer.accept(finalValue);
                    }
                });

        PairOfFloatsEditorWidget sizeWidget = new PairOfFloatsEditorWidget(40, editable, "width", value.width, "height", value.height,
                new PairOfFloatsEditorWidget.Callback() {
                    @Override
                    public void update(float value1, float value2) {
                        finalValue.width = value1;
                        finalValue.height = value2;
                        consumer.accept(finalValue);
                    }
                });

        table.add(positionWidget).growX().row();
        table.add(sizeWidget).growX().row();

        return table;
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        if (exact)
            return false;
        String stringType = type.asString();
        return stringType.equals("com.badlogic.gdx.math.Rectangle") || stringType.equals("Rectangle");
    }

    @Override
    public JsonValue convertToJson(Rectangle value) {
        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        result.addChild("x", new JsonValue(value.x));
        result.addChild("y", new JsonValue(value.y));
        result.addChild("width", new JsonValue(value.width));
        result.addChild("height", new JsonValue(value.height));
        return result;
    }

    @Override
    public Rectangle convertToValue(JsonValue json) {
        return new Rectangle(json.getFloat("x"), json.getFloat("y"),
                json.getFloat("width"), json.getFloat("height"));
    }

    @Override
    public Rectangle getDefaultValue() {
        return new Rectangle();
    }

    @Override
    public boolean isEditorSmall() {
        return false;
    }
}
