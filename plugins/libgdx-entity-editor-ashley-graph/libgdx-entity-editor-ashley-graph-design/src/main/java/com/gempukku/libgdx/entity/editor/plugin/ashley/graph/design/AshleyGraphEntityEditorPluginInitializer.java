package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.data.component.type.EnumFieldType;
import com.gempukku.libgdx.entity.editor.plugin.EntityEditorPluginInitializer;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.GraphSpritesPropertiesFieldType;
import com.gempukku.libgdx.entity.editor.project.ProjectReaderRegistry;
import com.gempukku.libgdx.graph.plugin.sprites.SpritesPluginRuntimeInitializer;

public class AshleyGraphEntityEditorPluginInitializer implements EntityEditorPluginInitializer {
    private AshleyGraphProjectReader projectReader = new AshleyGraphProjectReader();

    @Override
    public void initialize() {
        SpritesPluginRuntimeInitializer.register();
        ProjectReaderRegistry.register(projectReader);

        CustomFieldTypeRegistry.registerComponentFieldType(new EnumFieldType<>(FaceDirection.class, FaceDirection.Right));
        CustomFieldTypeRegistry.registerComponentFieldType(new GraphSpritesPropertiesFieldType());
    }
}
