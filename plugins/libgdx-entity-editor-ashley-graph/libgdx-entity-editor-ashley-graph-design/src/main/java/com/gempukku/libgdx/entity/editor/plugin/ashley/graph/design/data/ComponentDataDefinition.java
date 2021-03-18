package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.DefaultDataDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.AshleyGraphProject;

public abstract class ComponentDataDefinition<T extends Component, U extends ComponentDataStorage<T>> extends DefaultDataDefinition<U, T> {
    private AshleyGraphProject ashleyGraphProject;

    protected abstract U createComponentDataStorage(T component);

    protected abstract Class<T> getComponentClass();

    public ComponentDataDefinition(AshleyGraphProject ashleyGraphProject, String id, boolean component, String name, String className) {
        super(id, component, name, className);
        this.ashleyGraphProject = ashleyGraphProject;
    }

    @Override
    public T createDefaultValue() {
        return (T) ashleyGraphProject.createCoreComponent(getComponentClass());
    }

    @Override
    public T unpackFromDataStorage(U dataStorage) {
        return dataStorage.getComponent();
    }

    @Override
    public U wrapDataStorage(T value) {
        return createComponentDataStorage(value);
    }

    @Override
    public U loadDataStorage(Json json, JsonValue data) {
        return createComponentDataStorage(json.readValue(getComponentClass(), data));
    }

    @Override
    public JsonValue serializeDataStorage(U dataStorage) {
        Json json = new Json();
        JsonReader jsonReader = new JsonReader();
        return jsonReader.parse(json.toJson(dataStorage.getComponent(), getComponentClass()));
    }

    @Override
    public JsonValue exportComponent(U dataStorage) {
        return serializeDataStorage(dataStorage);
    }

    @Override
    public boolean isStoredWithProject() {
        return false;
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }
}
