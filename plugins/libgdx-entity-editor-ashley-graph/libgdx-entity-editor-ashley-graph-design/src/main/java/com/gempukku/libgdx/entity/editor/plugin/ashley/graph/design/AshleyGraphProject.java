package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroup;
import com.gempukku.libgdx.entity.editor.plugin.ObjectTreeFeedback;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;

public class AshleyGraphProject implements EntityEditorProject, ObjectTreeFeedback {
    private static final String PROJECT_FILE_NAME = "ashley-graph-entities.project.json";

    private EntityEditorScreen editorScreen;

    private Engine ashleyEngine;
    private AshleyGraphSettings settings;

    public static boolean hasProject(FileHandle folder) {
        return folder.child(PROJECT_FILE_NAME).exists();
    }

    @Override
    public void update(float delta) {
        ashleyEngine.update(delta);
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

        ashleyEngine = new Engine();
        createSettings(folder, readProject(folder), entityEditorScreen);

        entityEditorScreen.setPluginSettings(settings);
        entityEditorScreen.getObjectTreeData().setObjectTreeFeedback(this);
    }

    private JsonValue readProject(FileHandle folder) {
        JsonReader reader = new JsonReader();
        FileHandle child = folder.child(PROJECT_FILE_NAME);
        if (child.exists())
            return reader.parse(child);
        else
            return new JsonValue(JsonValue.ValueType.object);
    }

    private void createSettings(FileHandle folder, JsonValue project, EntityEditorScreen entityEditorScreen) {
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
    public void dispose() {
    }
}
