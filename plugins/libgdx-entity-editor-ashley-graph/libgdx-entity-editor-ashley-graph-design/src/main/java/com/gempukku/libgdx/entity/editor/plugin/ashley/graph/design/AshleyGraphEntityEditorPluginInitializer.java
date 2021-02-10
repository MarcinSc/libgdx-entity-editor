package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.gempukku.libgdx.entity.editor.data.component.EntityComponentRegistry;
import com.gempukku.libgdx.entity.editor.plugin.EntityEditorPluginInitializer;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FacingComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteStateComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.FacingComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.PositionComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ScaleComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.SpriteComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.project.ProjectReaderRegistry;
import com.gempukku.libgdx.graph.plugin.sprites.SpritesPluginRuntimeInitializer;

public class AshleyGraphEntityEditorPluginInitializer implements EntityEditorPluginInitializer {
    private AshleyGraphProjectReader projectReader = new AshleyGraphProjectReader();

    @Override
    public void initialize() {
        SpritesPluginRuntimeInitializer.register();
        ProjectReaderRegistry.register(projectReader);

        EntityComponentRegistry.registerCoreComponent(PositionComponent.class, new PositionComponentEditorFactory());
        EntityComponentRegistry.registerCoreComponent(ScaleComponent.class, new ScaleComponentEditorFactory());
        EntityComponentRegistry.registerCoreComponent(SpriteComponent.class, new SpriteComponentEditorFactory());
        EntityComponentRegistry.registerCoreComponent(FacingComponent.class, new FacingComponentEditorFactory());
        EntityComponentRegistry.registerCoreComponent(SpriteStateComponent.class, null);
    }
}
