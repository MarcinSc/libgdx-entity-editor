package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.utils.JsonValue;

public interface EntityDefinition<T> {
    String getId();

    String getName();

    Iterable<String> getTemplates();

    void addTemplate(String id);

    void removeTemplate(String id);

    Iterable<Class<? extends T>> getInheritedCoreComponents();

    T getInheritedCoreComponent(Class<? extends T> inheritedCoreComponentClass);

    void addCoreComponent(T coreComponent);

    void removeCoreComponent(Class<? extends T> coreComponent);

    Iterable<Class<? extends T>> getCoreComponents();

    boolean hasCoreComponent(Class<? extends T> coreComponent);

    T getCoreComponent(Class<? extends T> clazz);

    void rebuildEntity();

    JsonValue toJson();
}
