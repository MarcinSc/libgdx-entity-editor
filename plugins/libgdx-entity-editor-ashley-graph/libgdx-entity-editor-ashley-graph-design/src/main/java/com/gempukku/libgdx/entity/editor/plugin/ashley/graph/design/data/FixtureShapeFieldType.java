package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.BoxFixtureShape;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.FixtureShape;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.GraphSpriteProperties;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.widget.FixtureShapeEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class FixtureShapeFieldType implements ComponentFieldType<FixtureShape> {
    public static final String ID = "FixtureShape";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "FixtureShape";
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        String stringType = type.asString();
        return stringType.equals(FixtureShape.class.getName()) || stringType.equals(FixtureShape.class.getSimpleName());
    }

    @Override
    public Actor createEditor(boolean editable, FixtureShape value, Consumer<FixtureShape> consumer) {
        if (value == null) {
            value = getDefaultValue();
            consumer.accept(value);
        }
        return new FixtureShapeEditorWidget(editable, value, consumer);
    }

    @Override
    public JsonValue convertToJson(FixtureShape value) {
        JsonReader jsonReader = new JsonReader();
        Json json = new Json();
        json.setUsePrototypes(false);
        return jsonReader.parse(json.toJson(value, GraphSpriteProperties.class));
    }

    @Override
    public FixtureShape convertToValue(JsonValue json) {
        if (json.isNull())
            return getDefaultValue();

        Json jsonObj = new Json();
        return jsonObj.readValue(FixtureShape.class, json);
    }

    @Override
    public FixtureShape getDefaultValue() {
        return new BoxFixtureShape();
    }

    @Override
    public boolean isEditorSmall() {
        return false;
    }
}
