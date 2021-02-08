package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;

public class EntitySelected extends Event {
    private EntityDefinition entity;

    public EntitySelected(EntityDefinition entity) {
        this.entity = entity;
    }

    public EntityDefinition getEntity() {
        return entity;
    }
}
