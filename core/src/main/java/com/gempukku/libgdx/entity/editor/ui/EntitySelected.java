package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Event;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;

public class EntitySelected<T, U extends EntityDefinition<T>> extends Event {
    private U entity;
    private boolean isEntity;

    public EntitySelected(U entity, boolean isEntity) {
        this.entity = entity;
        this.isEntity = isEntity;
    }

    public boolean isEntity() {
        return isEntity;
    }

    public U getEntity() {
        return entity;
    }
}
