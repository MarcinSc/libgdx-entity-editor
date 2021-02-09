package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroup;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolder;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroup;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityTemplatesFolder;
import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system.CleaningSystem;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system.RenderingSystem;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.gempukku.libgdx.lib.template.ashley.AshleyEngineJson;

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

        JsonValue project = readProject(folder);
        createSettings(project, skin);
        entityEditorScreen.setPluginSettings(settings);

        ObjectTreeData objectTreeData = entityEditorScreen.getObjectTreeData();
        objectTreeData.setObjectTreeFeedback(this);

        setupProject(entityEditorScreen);

        AshleyEngineJson engineJson = new AshleyEngineJson(ashleyEngine);

        for (JsonValue entityGroup : project.get("entityGroups")) {
            String name = entityGroup.getString("name");
            for (JsonValue entity : entityGroup.get("entities")) {
                String path = entity.getString("path", null);
                Entity ashleyEntity = ashleyEngine.createEntity();
                JsonValue data = entity.get("data");
                AshleyEntityDefinition entityDefinition = new AshleyEntityDefinition(engineJson, ashleyEntity, data);
                objectTreeData.addEntity(name, path, entityDefinition.getName(), entityDefinition);
                ashleyEngine.addEntity(ashleyEntity);
            }
        }

        for (JsonValue template : project.get("templates")) {
            String path = template.getString("path", null);
            Entity ashleyEntity = ashleyEngine.createEntity();
            JsonValue data = template.get("data");
            AshleyEntityDefinition entityDefinition = new AshleyEntityDefinition(engineJson, ashleyEntity, data);
            objectTreeData.addTemplate(path, entityDefinition.getName(), entityDefinition);
        }
    }

    private void setupProject(EntityEditorScreen entityEditorScreen) {
        ashleyEngine = new Engine();
        ashleyEngine.addSystem(new CleaningSystem(100));
        timeKeeper = new DefaultTimeKeeper();
        directTextureLoader = new DirectTextureLoader(folder.child(settings.getAssetsFolder()));

        Entity entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);
        //ObjectTreeData objectTreeData = entityEditorScreen.getObjectTreeData();
        //objectTreeData.addEntity("level-1", null, "entity-1", new AshleyEntityDefinition("entity-1", entity));

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
    public void save(FileHandle folder, ObjectTreeData objectTreeData) {
        JsonValue project = new JsonValue(JsonValue.ValueType.object);

        JsonValue settingsValue = new JsonValue(JsonValue.ValueType.object);
        settingsValue.addChild("rendererPipeline", new JsonValue(settings.getRendererPipeline()));
        settingsValue.addChild("templatesFolder", new JsonValue(settings.getTemplatesFolder()));
        settingsValue.addChild("entityGroupsFolder", new JsonValue(settings.getEntityGroupsFolder()));
        settingsValue.addChild("assetsFolder", new JsonValue(settings.getAssetsFolder()));
        project.addChild("settings", settingsValue);

        JsonValue entityGroups = new JsonValue(JsonValue.ValueType.array);
        for (String entityGroup : objectTreeData.getEntityGroups()) {
            JsonValue group = new JsonValue(JsonValue.ValueType.object);
            group.addChild("name", new JsonValue(entityGroup));

            JsonValue entities = new JsonValue(JsonValue.ValueType.array);
            for (ObjectTreeData.LocatedEntityDefinition entity : objectTreeData.getEntities(entityGroup)) {
                JsonValue entityJson = new JsonValue(JsonValue.ValueType.object);
                entityJson.addChild("path", new JsonValue(entity.getPath()));
                entityJson.addChild("data", entity.getEntityDefinition().toJson());
                entities.addChild(entityJson);
            }
            group.addChild("entities", entities);

            entityGroups.addChild(group);
        }
        project.addChild("entityGroups", entityGroups);

        JsonValue templates = new JsonValue(JsonValue.ValueType.array);
        for (ObjectTreeData.LocatedEntityDefinition template : objectTreeData.getTemplates()) {
            JsonValue templateJson = new JsonValue(JsonValue.ValueType.object);
            templateJson.addChild("path", new JsonValue(template.getPath()));
            templateJson.addChild("data", template.getEntityDefinition().toJson());
            templates.addChild(templateJson);
        }

        project.addChild("templates", templates);

        folder.child(PROJECT_FILE_NAME).writeString(project.toJson(JsonWriter.OutputType.json), false);
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
    public EntityGroup createEntityGroup(String entityGroupName) {
        return new DefaultEntityGroup(entityGroupName);
    }

    @Override
    public EntityGroupFolder createEntityFolder(String name) {
        return new DefaultEntityGroupFolder(name);
    }

    @Override
    public EntityDefinition createEntity(String name) {
        Entity entity = ashleyEngine.createEntity();
        ashleyEngine.addEntity(entity);

        return new AshleyEntityDefinition(name, entity);
    }

    @Override
    public EntityTemplatesFolder createTemplatesFolder(String name) {
        return new DefaultEntityTemplatesFolder(name);
    }

    @Override
    public EntityDefinition createTemplate(String name) {
        Entity entity = ashleyEngine.createEntity();
        return new AshleyEntityDefinition(name, entity);
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
