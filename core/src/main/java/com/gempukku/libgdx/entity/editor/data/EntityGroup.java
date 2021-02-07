package com.gempukku.libgdx.entity.editor.data;

public interface EntityGroup {
    String getName();

    void createFolder(String parentPath, String name);

    void createEntity(String parentPath);
}
