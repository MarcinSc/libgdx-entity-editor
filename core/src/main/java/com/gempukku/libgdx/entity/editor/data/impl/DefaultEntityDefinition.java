package com.gempukku.libgdx.entity.editor.data.impl;

import com.gempukku.libgdx.entity.editor.data.EntityDefinition;

public abstract class DefaultEntityDefinition<T> implements EntityDefinition<T> {
    private String name;

    public DefaultEntityDefinition(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
