package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;

public class AshleyEntityComponent implements Component {
    private AshleyEntityDefinition entityDefinition;
    private boolean dirty;

    public AshleyEntityComponent(AshleyEntityDefinition entityDefinition) {
        this.entityDefinition = entityDefinition;
    }

    public AshleyEntityDefinition getEntityDefinition() {
        return entityDefinition;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
