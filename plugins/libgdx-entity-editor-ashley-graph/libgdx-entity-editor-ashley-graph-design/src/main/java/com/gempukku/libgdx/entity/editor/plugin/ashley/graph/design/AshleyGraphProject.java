package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityDefinition;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroup;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroupFolder;
import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;

import java.io.InputStream;

public class AshleyGraphProject implements EntityEditorProject, ObjectTreeFeedback {
    private static final String PROJECT_FILE_NAME = "ashley-graph-entities.project.json";

    private EntityEditorScreen editorScreen;

    private Engine ashleyEngine;
    private DefaultTimeKeeper timeKeeper;
    private GraphPreviewRenderer graphPreviewRenderer;

    private AshleyGraphSettings settings;

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

    public void initialize(FileHandle folder, EntityEditorScreen entityEditorScreen) {
        this.editorScreen = entityEditorScreen;

        createSettings(readProject(folder), entityEditorScreen);
        entityEditorScreen.setPluginSettings(settings);

        entityEditorScreen.getObjectTreeData().setObjectTreeFeedback(this);

        ashleyEngine = new Engine();
        timeKeeper = new DefaultTimeKeeper();

        FileHandle child = folder.child(settings.getRendererPipeline());
        try {
            InputStream inputStream = child.read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(inputStream, new PipelineLoaderCallback(timeKeeper));
                graphPreviewRenderer = new GraphPreviewRenderer(pipelineRenderer);
                entityEditorScreen.setPreviewRenderer(graphPreviewRenderer);
            } finally {
                inputStream.close();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    private JsonValue readProject(FileHandle folder) {
        JsonReader reader = new JsonReader();
        FileHandle child = folder.child(PROJECT_FILE_NAME);
        if (child.exists())
            return reader.parse(child);
        else
            return new JsonValue(JsonValue.ValueType.object);
    }

    private void createSettings(JsonValue project, EntityEditorScreen entityEditorScreen) {
        JsonValue settings = project.get("settings");
        if (settings != null) {
            String rendererPipeline = settings.getString("rendererPipeline", null);
            String templatesFolder = settings.getString("templatesFolder", null);
            String entityGroupsFolder = settings.getString("entityGroupsFolder", null);
            String assetsFolder = settings.getString("assetsFolder", null);

            this.settings = new AshleyGraphSettings(entityEditorScreen.getSkin(),
                    rendererPipeline, templatesFolder, entityGroupsFolder, assetsFolder);
        } else {
            this.settings = new AshleyGraphSettings(entityEditorScreen.getSkin(),
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
        DefaultEntityDefinition entityDefinition = new DefaultEntityDefinition(name);
        folder.createEntity(entityDefinition);
        return entityDefinition;
    }

    @Override
    public void dispose() {
        if (graphPreviewRenderer != null) {
            graphPreviewRenderer.dispose();
        }
    }
}
