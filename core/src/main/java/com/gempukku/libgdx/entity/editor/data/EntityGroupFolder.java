package com.gempukku.libgdx.entity.editor.data;

public interface EntityGroupFolder {
    String getName();

    Iterable<EntityGroupFolder> getFolders();

    Iterable<EntityDefinition> getEntities();

    void createFolder(EntityGroupFolder folder);

    void createEntity(EntityDefinition entity);
}
