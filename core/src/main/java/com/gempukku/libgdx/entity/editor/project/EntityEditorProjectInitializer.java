package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.files.FileHandle;

public interface EntityEditorProjectInitializer {
    String getTemplateName();

    boolean canReadProject(FileHandle folder);

    EntityEditorProject createNewProject(FileHandle folder);

    EntityEditorProject openProject(FileHandle folder);
}
