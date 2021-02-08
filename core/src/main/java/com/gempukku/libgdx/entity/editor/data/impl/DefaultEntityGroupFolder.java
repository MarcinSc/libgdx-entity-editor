package com.gempukku.libgdx.entity.editor.data.impl;

import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;

public class DefaultEntityGroupFolder implements EntityGroupFolder {
    private String name;
    private Array<EntityGroupFolder> folders = new Array<>();
    private Array<EntityDefinition> entities = new Array<>();

    public DefaultEntityGroupFolder(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Iterable<EntityGroupFolder> getFolders() {
        return folders;
    }

    @Override
    public Iterable<EntityDefinition> getEntities() {
        return entities;
    }

    @Override
    public void createFolder(EntityGroupFolder folder) {
        folders.add(folder);
    }

    @Override
    public void createEntity(EntityDefinition entity) {
        entities.add(entity);
    }
}
