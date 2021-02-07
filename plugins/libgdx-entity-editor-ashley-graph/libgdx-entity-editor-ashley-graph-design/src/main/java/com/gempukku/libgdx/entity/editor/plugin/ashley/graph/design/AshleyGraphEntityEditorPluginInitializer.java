package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.gempukku.libgdx.entity.editor.plugin.EntityEditorPluginInitializer;
import com.gempukku.libgdx.entity.editor.project.ProjectReaderRegistry;

public class AshleyGraphEntityEditorPluginInitializer implements EntityEditorPluginInitializer {
    private AshleyGraphProjectReader projectReader = new AshleyGraphProjectReader();

    @Override
    public void initialize() {
        ProjectReaderRegistry.register(projectReader);
    }
}
