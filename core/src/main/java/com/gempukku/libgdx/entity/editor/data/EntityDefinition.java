package com.gempukku.libgdx.entity.editor.data;

import com.gempukku.libgdx.entity.editor.data.component.DataStorage;

public interface EntityDefinition {
    String getId();

    String getName();

    void setName(String name);

    boolean isEntity();

    Iterable<String> getTemplates();

    boolean hasTemplate(String id);

    void addTemplate(String id);

    void removeTemplate(String id);

    Iterable<String> getInheritedComponents();

    DataStorage getInheritedComponent(String componentId);

    Iterable<String> getComponents();

    void addComponent(String id, DataStorage dataStorage);

    void removeComponent(String componentId);

    boolean hasComponent(String componentId);

    DataStorage getComponent(String componentId);
}
