package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.data.component.type.EnumFieldType;
import com.gempukku.libgdx.entity.editor.plugin.EntityEditorPluginInitializer;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.FixtureShapeFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.GraphSpritesPropertiesFieldType;
import com.gempukku.libgdx.entity.editor.project.ProjectReaderRegistry;
import com.gempukku.libgdx.graph.plugin.lighting3d.Lighting3DPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.maps.MapsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.models.ModelsPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.particles.ParticlesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.screen.ScreenPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.sprites.SpritesPluginRuntimeInitializer;
import com.gempukku.libgdx.graph.plugin.ui.UIPluginRuntimeInitializer;

public class AshleyGraphEntityEditorPluginInitializer implements EntityEditorPluginInitializer {
    private AshleyGraphProjectReader projectReader = new AshleyGraphProjectReader();

    @Override
    public void initialize() {
        Lighting3DPluginRuntimeInitializer.register();
        ModelsPluginRuntimeInitializer.register();
        SpritesPluginRuntimeInitializer.register();
        ParticlesPluginRuntimeInitializer.register();
        ScreenPluginRuntimeInitializer.register();
        MapsPluginRuntimeInitializer.register();
        UIPluginRuntimeInitializer.register();

        ProjectReaderRegistry.register(projectReader);

        CustomFieldTypeRegistry.registerComponentFieldType(new EnumFieldType<>(FaceDirection.class, FaceDirection.Right));
        CustomFieldTypeRegistry.registerComponentFieldType(new GraphSpritesPropertiesFieldType());

        CustomFieldTypeRegistry.registerComponentFieldType(new EnumFieldType<>(BodyDef.BodyType.class, BodyDef.BodyType.StaticBody));
        CustomFieldTypeRegistry.registerComponentFieldType(new FixtureShapeFieldType());
    }
}
