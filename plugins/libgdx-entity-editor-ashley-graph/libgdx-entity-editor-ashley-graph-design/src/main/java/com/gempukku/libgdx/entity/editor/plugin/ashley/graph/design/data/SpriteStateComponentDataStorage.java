package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;

public class SpriteStateComponentDataStorage extends ComponentDataStorage<SpriteStateComponent> {
    public SpriteStateComponentDataStorage(SpriteStateComponent component) {
        super(component);
    }

    @Override
    public ComponentDataStorage<SpriteStateComponent> createDataStorage(SpriteStateComponent component) {
        return new SpriteStateComponentDataStorage(component);
    }

    @Override
    public JsonValue getValue(String fieldName) {
        if (fieldName.equals("state"))
            return new JsonValue(getComponent().getState());
        else if (fieldName.equals("states")) {
            Json json = new Json();
            json.setUsePrototypes(false);
            JsonReader jsonReader = new JsonReader();

            JsonValue jsonValue = new JsonValue(JsonValue.ValueType.object);
            ObjectMap<String, SpriteStateDataDef> states = getComponent().getStates();
            for (ObjectMap.Entry<String, SpriteStateDataDef> state : states) {
                jsonValue.addChild(state.key, jsonReader.parse(json.toJson(state.value, SpriteStateDataDef.class)));
            }
            return jsonValue;
        } else
            throw new IllegalArgumentException();
    }

    @Override
    public void setValue(String fieldName, JsonValue value) {
        if (fieldName.equals("state"))
            getComponent().setState(value.asString());
        else if (fieldName.equals("states")) {
            Json json = new Json();
            json.setUsePrototypes(false);

            ObjectMap<String, SpriteStateDataDef> result = new ObjectMap<>();
            for (JsonValue entry : value) {
                result.put(entry.name(), json.readValue(SpriteStateDataDef.class, entry));
            }
            getComponent().setStates(result);
        } else
            throw new IllegalArgumentException();
    }
}
