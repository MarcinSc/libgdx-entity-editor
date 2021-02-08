package com.gempukku.libgdx.entity.editor.data.impl;

import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolder;

public class DefaultEntityTemplatesFolder implements EntityTemplatesFolder {
    private String name;

    public DefaultEntityTemplatesFolder(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
