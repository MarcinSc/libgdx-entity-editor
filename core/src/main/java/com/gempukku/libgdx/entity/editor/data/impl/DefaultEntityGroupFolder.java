package com.gempukku.libgdx.entity.editor.data.impl;

import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;

public class DefaultEntityGroupFolder implements EntityGroupFolder {
    private String name;

    public DefaultEntityGroupFolder(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
