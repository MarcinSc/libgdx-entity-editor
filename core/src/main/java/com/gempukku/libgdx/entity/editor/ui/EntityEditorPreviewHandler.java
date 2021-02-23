package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;

public interface EntityEditorPreviewHandler {
    void initialize(EntityEditorScreen preview);

    void destroy(EntityEditorScreen preview);

    void render(Batch batch, float x, float y, float width, float height);

    <T, U extends EntityDefinition<T>> void setEditedEntity(U editedEntity, EntityEditorProject<T, U> project, boolean entity);
}
