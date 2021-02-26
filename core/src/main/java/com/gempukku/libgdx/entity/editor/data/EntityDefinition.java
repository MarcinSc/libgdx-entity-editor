package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.utils.ObjectMap;

public interface EntityDefinition<T> {
    String getId();

    String getName();

    void setName(String name);

    boolean isEntity();

    Iterable<String> getTemplates();

    boolean hasTemplate(String id);

    void addTemplate(String id);

    void removeTemplate(String id);

    Iterable<Class<? extends T>> getInheritedCoreComponents();

    T getInheritedCoreComponent(Class<? extends T> inheritedCoreComponentClass);

    void addCoreComponent(T coreComponent);

    void removeCoreComponent(Class<? extends T> coreComponent);

    Iterable<Class<? extends T>> getCoreComponents();

    boolean hasCoreComponent(Class<? extends T> coreComponent);

    T getCoreComponent(Class<? extends T> clazz);

    boolean hasCustomComponent(String id);

    void addCustomComponent(String id, ObjectMap<String, Object> componentData);

    ObjectMap<String, ObjectMap<String, Object>> getInheritedCustomComponents();

    ObjectMap<String, ObjectMap<String, Object>> getCustomComponents();
}
