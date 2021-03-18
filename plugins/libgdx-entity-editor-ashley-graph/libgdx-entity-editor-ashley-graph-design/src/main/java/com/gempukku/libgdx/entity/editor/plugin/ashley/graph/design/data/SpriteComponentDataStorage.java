package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.GraphSpriteProperties;

public class SpriteComponentDataStorage extends ComponentDataStorage<SpriteComponent> {
    public SpriteComponentDataStorage(SpriteComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<SpriteComponent> createDataStorage(SpriteComponent component) {
        return new SpriteComponentDataStorage(component);
    }

    @Override
    public JsonValue getValue(String fieldName) {
        if (fieldName.equals("width"))
            return new JsonValue(getComponent().getWidth());
        else if (fieldName.equals("height"))
            return new JsonValue(getComponent().getHeight());
        else if (fieldName.equals("layer"))
            return new JsonValue(getComponent().getLayer());
        else if (fieldName.equals("tags")) {
            JsonValue result = new JsonValue(JsonValue.ValueType.array);
            for (String tag : getComponent().getTags()) {
                result.addChild(new JsonValue(tag));
            }
            return result;
        } else if (fieldName.equals("properties")) {
            Json json = new Json();
            JsonReader jsonReader = new JsonReader();
            return jsonReader.parse(json.toJson(getComponent().getProperties(), GraphSpriteProperties.class));
        } else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, JsonValue value) {
        if (fieldName.equals("width"))
            getComponent().setWidth(value.asFloat());
        else if (fieldName.equals("height"))
            getComponent().setHeight(value.asFloat());
        else if (fieldName.equals("layer"))
            getComponent().setLayer(value.asFloat());
        else if (fieldName.equals("tags"))
            getComponent().setTags(new Array<>(value.asStringArray()));
        else if (fieldName.equals("properties")) {
            Json json = new Json();
            getComponent().setProperties(json.readValue(GraphSpriteProperties.class, value));
        } else
            throw new IllegalArgumentException();
    }
}
