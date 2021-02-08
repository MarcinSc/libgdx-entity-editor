package com.gempukku.libgdx.entity.editor.data.impl;

import com.gempukku.libgdx.entity.editor.data.EntityDefinition;

public class DefaultEntityDefinition implements EntityDefinition {
    private String name;

    public DefaultEntityDefinition(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
