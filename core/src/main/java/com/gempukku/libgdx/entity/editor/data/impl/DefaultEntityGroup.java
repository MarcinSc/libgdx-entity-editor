package com.gempukku.libgdx.entity.editor.data.impl;

import com.gempukku.libgdx.entity.editor.data.EntityGroup;

public class DefaultEntityGroup implements EntityGroup {
    private String name;

    public DefaultEntityGroup(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void createFolder(String parentPath, String name) {

    }

    @Override
    public void createEntity(String parentPath) {

    }
}
