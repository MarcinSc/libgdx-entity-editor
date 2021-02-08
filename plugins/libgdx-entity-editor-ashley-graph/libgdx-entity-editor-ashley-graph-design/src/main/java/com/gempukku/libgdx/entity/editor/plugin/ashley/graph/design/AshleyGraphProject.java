package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityDefinition;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroup;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroupFolder;
import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system.RenderingSystem;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.WhitePixel;

import java.io.InputStream;

public class AshleyGraphProject implements EntityEditorProject, ObjectTreeFeedback {
    private static final String PROJECT_FILE_NAME = "ashley-graph-entities.project.json";

    private EntityEditorScreen editorScreen;
    private WhitePixel whitePixel;

    private Engine ashleyEngine;
    private DefaultTimeKeeper timeKeeper;
    private GraphPreviewRenderer graphPreviewRenderer;
    private DirectTextureLoader directTextureLoader;

    private FileHandle folder;
    private AshleyGraphSettings settings;

    public AshleyGraphProject(FileHandle folder) {
        this.folder = folder;
    }

    @Override
    public void initialize(Skin skin, EntityEditorScreen entityEditorScreen) {
        this.whitePixel = new WhitePixel();
        this.editorScreen = entityEditorScreen;

        createSettings(readProject(folder), skin);
        entityEditorScreen.setPluginSettings(settings);

        entityEditorScreen.getObjectTreeData().setObjectTreeFeedback(this);

        setupProject(entityEditorScreen);
    }

    private void setupProject(EntityEditorScreen entityEditorScreen) {
        ashleyEngine = new Engine();
        timeKeeper = new DefaultTimeKeeper();
        directTextureLoader = new DirectTextureLoader();

        ObjectTreeData objectTreeData = entityEditorScreen.getObjectTreeData();
        objectTreeData.addEntityGroup(new DefaultEntityGroup("level-1"));
        Entity entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        objectTreeData.addEntity("level-1", null, "entity-1", new AshleyEntityDefinition("entity-1", entity));

        FileHandle child = folder.child(settings.getRendererPipeline());
        try {
            InputStream inputStream = child.read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(inputStream, new PipelineLoaderCallback(timeKeeper));
                // TODO temporary camera
                pipelineRenderer.setPipelineProperty("Camera", entityEditorScreen.getCamera());

                RenderingSystem renderingSystem = new RenderingSystem(0, timeKeeper, pipelineRenderer, directTextureLoader);
                renderingSystem.setDefaultTextureRegion(whitePixel.textureRegion);
                ashleyEngine.addSystem(renderingSystem);
                graphPreviewRenderer = new GraphPreviewRenderer(pipelineRenderer);
                entityEditorScreen.setPreviewRenderer(graphPreviewRenderer);
            } finally {
                inputStream.close();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public static boolean hasProject(FileHandle folder) {
        return folder.child(PROJECT_FILE_NAME).exists();
    }

    @Override
    public void update(float delta) {
        timeKeeper.updateTime(delta);
        ashleyEngine.update(delta);

        int previewWidth = editorScreen.getPreviewWidth();
        int previewHeight = editorScreen.getPreviewHeight();

        graphPreviewRenderer.prepare(previewWidth, previewHeight);
    }

    @Override
    public void save(FileHandle folder) {
        JsonValue project = new JsonValue(JsonValue.ValueType.object);

        JsonValue settingsValue = new JsonValue(JsonValue.ValueType.object);
        settingsValue.addChild("rendererPipeline", new JsonValue(settings.getRendererPipeline()));
        settingsValue.addChild("templatesFolder", new JsonValue(settings.getTemplatesFolder()));
        settingsValue.addChild("entityGroupsFolder", new JsonValue(settings.getEntityGroupsFolder()));
        settingsValue.addChild("assetsFolder", new JsonValue(settings.getAssetsFolder()));
        project.addChild("settings", settingsValue);
    }

    private JsonValue readProject(FileHandle folder) {
        JsonReader reader = new JsonReader();
        FileHandle child = folder.child(PROJECT_FILE_NAME);
        if (child.exists())
            return reader.parse(child);
        else
            return new JsonValue(JsonValue.ValueType.object);
    }

    private void createSettings(JsonValue project, Skin skin) {
        JsonValue settings = project.get("settings");
        if (settings != null) {
            String rendererPipeline = settings.getString("rendererPipeline", null);
            String templatesFolder = settings.getString("templatesFolder", null);
            String entityGroupsFolder = settings.getString("entityGroupsFolder", null);
            String assetsFolder = settings.getString("assetsFolder", null);

            this.settings = new AshleyGraphSettings(skin,
                    rendererPipeline, templatesFolder, entityGroupsFolder, assetsFolder);
        } else {
            this.settings = new AshleyGraphSettings(skin,
                    "assets/pipeline.json", "assets/templates",
                    "assets/entities", "assets");
        }
    }

    @Override
    public void createEntityGroup(String entityGroupName) {
        editorScreen.getObjectTreeData().addEntityGroup(new DefaultEntityGroup(entityGroupName));
    }

    @Override
    public EntityGroupFolder createEntityFolder(EntityGroupFolder folder, String name) {
        DefaultEntityGroupFolder result = new DefaultEntityGroupFolder(name);
        folder.createFolder(result);
        return result;
    }

    @Override
    public EntityDefinition createEntity(EntityGroupFolder folder, String name) {
        Entity entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);

        DefaultEntityDefinition entityDefinition = new AshleyEntityDefinition(name, entity);
        folder.createEntity(entityDefinition);
        return entityDefinition;
    }

    @Override
    public boolean supportsComponent(Class<?> componentClass) {
        return ClassReflection.isAssignableFrom(Component.class, componentClass);
    }

    @Override
    public Object createCoreComponent(Class<?> coreComponent) {
        Component component = ashleyEngine.createComponent((Class<Component>) coreComponent);
        if (component instanceof SpriteComponent)
            ((SpriteComponent) component).addTag("Animated");
        return component;
    }

    @Override
    public void dispose() {
        if (graphPreviewRenderer != null) {
            graphPreviewRenderer.dispose();
        }
        directTextureLoader.dispose();
        whitePixel.dispose();
    }
}
