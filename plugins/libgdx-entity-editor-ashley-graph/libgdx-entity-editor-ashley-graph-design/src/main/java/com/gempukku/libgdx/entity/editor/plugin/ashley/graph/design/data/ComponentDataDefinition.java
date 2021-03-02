package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.DefaultDataDefinition;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;

public abstract class ComponentDataDefinition<T extends Component, U extends ComponentDataStorage<T>> extends DefaultDataDefinition<U> {
    protected abstract U createComponentDataStorage(T component);

    protected abstract Class<T> getComponentClass();

    public ComponentDataDefinition(String id, boolean component, String name, String className) {
        super(id, component, name, className);
    }

    @Override
    public U createDataStorage(EntityEditorProject project) {
        return createComponentDataStorage((T) project.createCoreComponent(getComponentClass()));
    }

    @Override
    public U loadDataStorage(Json json, JsonValue data) {
        return createComponentDataStorage(json.readValue(getComponentClass(), data));
    }

    @Override
    public JsonValue serializeDataStorage(Json json, U dataStorage) {
        JsonReader jsonReader = new JsonReader();
        return jsonReader.parse(json.toJson(dataStorage.getComponent(), getComponentClass()));
    }

    @Override
    public JsonValue exportComponent(Json json, U dataStorage) {
        return serializeDataStorage(json, dataStorage);
    }

    @Override
    public boolean isStoreWithProject() {
        return false;
    }
}
