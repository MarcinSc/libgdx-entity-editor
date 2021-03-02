package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.entity.editor.data.component.DataStorage;

public abstract class ComponentDataStorage<T extends Component> implements DataStorage {
    private T component;

    public ComponentDataStorage(T component) {
        this.component = component;
    }

    public T getComponent() {
        return component;
    }

    public abstract ComponentDataStorage<T> createDataStorage(T component);

    public ComponentDataStorage<T> createCopy() {
        Component component = getComponent();
        Json json = new Json(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        return createDataStorage((T) json.fromJson(Component.class, json.toJson(component, Component.class)));
    }
}
