package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.data.component.type.EnumFieldType;
import com.gempukku.libgdx.entity.editor.plugin.EntityEditorPluginInitializer;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.FixtureShapeFieldType;
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

        CustomFieldTypeRegistry.registerComponentFieldType(new EnumFieldType<>(BodyDef.BodyType.class, BodyDef.BodyType.StaticBody));
        CustomFieldTypeRegistry.registerComponentFieldType(new FixtureShapeFieldType());
    }
}
