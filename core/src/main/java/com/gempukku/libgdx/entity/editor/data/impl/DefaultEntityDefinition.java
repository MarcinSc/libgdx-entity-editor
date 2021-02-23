package com.gempukku.libgdx.entity.editor.data.impl;

import com.gempukku.libgdx.entity.editor.data.EntityDefinition;

public abstract class DefaultEntityDefinition<T> implements EntityDefinition<T> {
    private String id;
    private String name;
    private boolean entity;

    public DefaultEntityDefinition(String id, String name, boolean entity) {
        this.id = id;
        this.name = name;
        this.entity = entity;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isEntity() {
        return entity;
    }
}
