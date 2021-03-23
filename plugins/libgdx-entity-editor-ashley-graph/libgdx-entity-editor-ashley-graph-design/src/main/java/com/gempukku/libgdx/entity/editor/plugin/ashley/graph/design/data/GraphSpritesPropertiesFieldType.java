package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.GraphSpriteProperties;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.widget.GraphSpritePropertiesEditorWidget;
import com.github.javaparser.ast.type.Type;

import java.util.function.Consumer;

public class GraphSpritesPropertiesFieldType implements ComponentFieldType<GraphSpriteProperties> {
    public static final String ID = "GraphSpritesProperties";

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Graph Sprites Properties";
    }

    @Override
    public boolean accepts(String componentClass, String fieldName, Type type, boolean exact) {
        String stringType = type.asString();
        return stringType.equals(GraphSpriteProperties.class.getName()) || stringType.equals(GraphSpriteProperties.class.getSimpleName());
    }

    @Override
    public Actor createEditor(boolean editable, GraphSpriteProperties value, Consumer<GraphSpriteProperties> consumer) {
        if (value == null) {
            value = getDefaultValue();
            consumer.accept(value);
        }
        return new GraphSpritePropertiesEditorWidget(editable, value, consumer);
    }

    @Override
    public JsonValue convertToJson(GraphSpriteProperties value) {
        JsonReader jsonReader = new JsonReader();
        Json json = new Json();
        json.setUsePrototypes(false);
        return jsonReader.parse(json.toJson(value, GraphSpriteProperties.class));
    }

    @Override
    public GraphSpriteProperties convertToValue(JsonValue json) {
        if (json.isNull())
            return getDefaultValue();

        Json jsonObj = new Json();
        return jsonObj.readValue(GraphSpriteProperties.class, json);
    }

    @Override
    public GraphSpriteProperties getDefaultValue() {
        return new GraphSpriteProperties();
    }

    @Override
    public boolean isEditorSmall() {
        return false;
    }
}
