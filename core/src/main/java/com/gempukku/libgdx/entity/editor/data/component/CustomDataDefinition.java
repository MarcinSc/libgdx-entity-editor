package com.gempukku.libgdx.entity.editor.data.component;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;

public class CustomDataDefinition extends DefaultDataDefinition<CustomDataStorage> {
    public CustomDataDefinition(String id, boolean component, String name, String className) {
        super(id, component, name, className);
    }

    @Override
    public CustomDataStorage createDataStorage(EntityEditorProject project) {
        return new CustomDataStorage();
    }

    @Override
    public CustomDataStorage loadDataStorage(Json json, JsonValue data) {
        return new CustomDataStorage(json.readValue(ObjectMap.class, data));
    }

    @Override
    public JsonValue serializeDataStorage(Json json, CustomDataStorage dataStorage) {
        JsonReader jsonReader = new JsonReader();
        return jsonReader.parse(json.toJson(dataStorage.getData(), ObjectMap.class));
    }

    @Override
    public JsonValue exportComponent(Json json, CustomDataStorage dataStorage) {
        return serializeDataStorage(json, dataStorage);
    }

    @Override
    public boolean isStoreWithProject() {
        return true;
    }
}
