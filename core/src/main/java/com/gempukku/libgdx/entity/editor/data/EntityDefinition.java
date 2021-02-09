package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.component.CustomComponentDefinition;

public interface EntityDefinition<T> {
    String getName();

    void addCoreComponent(T coreComponent);

    void removeCoreComponent(T coreComponent);

    Iterable<? extends T> getCoreComponents();

    Iterable<CustomComponentDefinition> getCustomComponents();

    boolean hasCoreComponent(Class<T> coreComponent);

    JsonValue toJson();
}
