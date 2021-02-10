package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.utils.JsonValue;

public interface EntityDefinition<T> {
    String getName();

    void addCoreComponent(T coreComponent);

    void removeCoreComponent(Class<? extends T> coreComponent);

    Iterable<Class<? extends T>> getCoreComponents();

    boolean hasCoreComponent(Class<? extends T> coreComponent);

    T getCoreComponent(Class<? extends T> clazz);

    JsonValue toJson();
}
