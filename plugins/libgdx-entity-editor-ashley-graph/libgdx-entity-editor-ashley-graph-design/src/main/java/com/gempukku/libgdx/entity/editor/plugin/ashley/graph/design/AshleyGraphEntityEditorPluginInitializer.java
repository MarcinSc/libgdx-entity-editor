package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.plugin.EntityEditorPluginInitializer;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.FaceDirectionFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.SpriteComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.SpriteStateComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.project.ProjectReaderRegistry;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditorRegistry;
import com.gempukku.libgdx.graph.plugin.sprites.SpritesPluginRuntimeInitializer;

public class AshleyGraphEntityEditorPluginInitializer implements EntityEditorPluginInitializer {
    private AshleyGraphProjectReader projectReader = new AshleyGraphProjectReader();

    @Override
    public void initialize() {
        SpritesPluginRuntimeInitializer.register();
        ProjectReaderRegistry.register(projectReader);

        CustomFieldTypeRegistry.registerComponentFieldType(new FaceDirectionFieldType());
        ComponentEditorRegistry.registerComponentEditorFactory("SpriteComponent", new SpriteComponentEditorFactory());
        ComponentEditorRegistry.registerComponentEditorFactory("SpriteStateComponent", new SpriteStateComponentEditorFactory());
    }
}
