package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;

public class
EntitySelected extends Event {
    private EntityDefinition entity;
    private boolean isEntity;

    public EntitySelected(EntityDefinition entity, boolean isEntity) {
        this.entity = entity;
        this.isEntity = isEntity;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public EntityDefinition getEntity() {
        return entity;
    }
}
