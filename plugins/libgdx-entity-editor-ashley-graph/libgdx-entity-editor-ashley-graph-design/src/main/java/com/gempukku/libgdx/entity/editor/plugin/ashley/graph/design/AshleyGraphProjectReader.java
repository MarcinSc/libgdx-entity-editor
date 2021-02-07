package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProjectInitializer;

public class AshleyGraphProjectReader implements EntityEditorProjectInitializer {
    @Override
    public String getTemplateName() {
        return "libGDX-graph rendering, Ashley ECS";
    }

    @Override
    public boolean canReadProject(FileHandle folder) {
        return AshleyGraphProject.hasProject(folder);
    }

    @Override
    public EntityEditorProject createNewProject(FileHandle folder, EntityEditorScreen entityEditorScreen) {
        if (canReadProject(folder))
            throw new GdxRuntimeException("There is already a project in that folder");
        AshleyGraphProject ashleyGraphProject = new AshleyGraphProject();
        ashleyGraphProject.initialize(folder, entityEditorScreen);
        return ashleyGraphProject;
    }

    @Override
    public EntityEditorProject openProject(FileHandle folder, EntityEditorScreen entityEditorScreen) {
        AshleyGraphProject ashleyGraphProject = new AshleyGraphProject();
        ashleyGraphProject.initialize(folder, entityEditorScreen);
        return ashleyGraphProject;
    }
}
