package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.utils.JsonValue;

public interface EntityDefinition<T> {
    String getId();

    String getName();

    Iterable<Class<? extends T>> getInheritedCoreComponents(ObjectTreeData treeData);

    void addCoreComponent(T coreComponent);

    void removeCoreComponent(Class<? extends T> coreComponent);

    Iterable<Class<? extends T>> getCoreComponents();

    boolean hasCoreComponent(Class<? extends T> coreComponent);

    T getCoreComponent(Class<? extends T> clazz);

    JsonValue toJson();

    T getInheritedCoreComponent(ObjectTreeData treeData, Class<? extends T> inheritedCoreComponentClass);
}
