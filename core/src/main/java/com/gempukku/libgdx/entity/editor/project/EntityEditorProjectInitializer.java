package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.files.FileHandle;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;

public interface EntityEditorProjectInitializer {
    String getTemplateName();

    boolean canReadProject(FileHandle folder);

    EntityEditorProject createNewProject(FileHandle folder, EntityEditorScreen entityEditorScreen);

    EntityEditorProject openProject(FileHandle folder, EntityEditorScreen entityEditorScreen);
}
