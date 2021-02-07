package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.gempukku.libgdx.entity.editor.data.EntityComponentRegistry;
import com.gempukku.libgdx.entity.editor.plugin.EntityEditorPluginInitializer;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FacingComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;
import com.gempukku.libgdx.entity.editor.project.ProjectReaderRegistry;

public class AshleyGraphEntityEditorPluginInitializer implements EntityEditorPluginInitializer {
    private AshleyGraphProjectReader projectReader = new AshleyGraphProjectReader();

    @Override
    public void initialize() {
        ProjectReaderRegistry.register(projectReader);

        EntityComponentRegistry.registerCoreComponent(FacingComponent.class);
        EntityComponentRegistry.registerCoreComponent(PositionComponent.class);
        EntityComponentRegistry.registerCoreComponent(ScaleComponent.class);
        EntityComponentRegistry.registerCoreComponent(SpriteComponent.class);
        EntityComponentRegistry.registerCoreComponent(SpriteStateComponent.class);
    }
}
