package com.gempukku.libgdx.entity.editor.data;

public interface EntityDefinition<T> {
    String getId();

    String getName();

    void setName(String name);

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
}
