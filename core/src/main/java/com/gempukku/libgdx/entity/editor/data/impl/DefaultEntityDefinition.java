package com.gempukku.libgdx.entity.editor.data.impl;

import com.gempukku.libgdx.entity.editor.data.EntityDefinition;

public abstract class DefaultEntityDefinition<T> implements EntityDefinition<T> {
    private String id;
    private String name;

    public DefaultEntityDefinition(String id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
