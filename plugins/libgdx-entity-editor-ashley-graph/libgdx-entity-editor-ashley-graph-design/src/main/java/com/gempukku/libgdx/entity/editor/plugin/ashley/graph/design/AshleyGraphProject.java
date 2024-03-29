package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.EntityGroup;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolder;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.component.CustomClassDataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.CustomDataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.DataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.gempukku.libgdx.entity.editor.data.component.type.BooleanComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.type.FloatComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.type.StringComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroup;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityTemplatesFolder;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.FixtureDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.FixtureShape;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.AnchorComponentDataDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.Box2DBodyComponentDataDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.FacingComponentDataDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.GraphSpritesPropertiesFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.PositionComponentDataDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.ScaleComponentDataDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.SpriteComponentDataDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.SpriteStateComponentDataDefinition;
import com.gempukku.libgdx.entity.editor.project.EntityEditorProject;
import com.gempukku.libgdx.entity.editor.ui.EntityInspector;
import com.gempukku.libgdx.entity.editor.ui.ObjectTree;
import com.gempukku.libgdx.graph.loader.GraphLoader;
import com.gempukku.libgdx.graph.pipeline.PipelineLoaderCallback;
import com.gempukku.libgdx.graph.pipeline.PipelineRenderer;
import com.gempukku.libgdx.graph.shader.common.CommonShaderConfiguration;
import com.gempukku.libgdx.graph.time.DefaultTimeKeeper;
import com.gempukku.libgdx.graph.util.WhitePixel;
import com.gempukku.libgdx.lib.template.ashley.AshleyEngineJson;

import java.io.InputStream;

public class AshleyGraphProject implements EntityEditorProject<Component, AshleyEntityDefinition> {
    private static final String PROJECT_FILE_NAME = "ashley-graph-entities.project.json";

    private WhitePixel whitePixel;

    private Engine ashleyEngine;
    private World world;
    private float pixelsToMeters = 100;
    private DefaultTimeKeeper timeKeeper;
    private GraphPreviewRenderer graphPreviewRenderer;
    private DirectTextureLoader directTextureLoader;

    private FileHandle folder;
    private AshleyGraphSettings settings;
    private EntityInspector<Component, AshleyEntityDefinition> entityInspector;
    private ObjectTreeData<AshleyEntityDefinition> objectTreeData;
    private AshleyEngineJson engineJson;

    public AshleyGraphProject(FileHandle folder) {
        this.folder = folder;
    }

    @Override
    public void initialize(EntityEditorScreen<Component, AshleyEntityDefinition> entityEditorScreen) {
        this.whitePixel = new WhitePixel();
        CommonShaderConfiguration.setDefaultTextureRegionProperty(whitePixel.textureRegion);

        JsonValue project = readProject(folder);
        createSettings(project);
        entityEditorScreen.setPluginSettings(settings);

        objectTreeData = entityEditorScreen.getObjectTreeData();
        entityInspector = entityEditorScreen.getEntityInspector();

        ashleyEngine = new Engine();
        ashleyEngine.addSystem(new CleaningSystem(100));

        world = new World(new Vector2(0, 0), true);

        timeKeeper = new DefaultTimeKeeper();
        directTextureLoader = new DirectTextureLoader(folder.child(settings.getAssetsFolder()));

        entityEditorScreen.setDefaultPreviewHandler(new AshleyGraphPreviewHandler(this, ashleyEngine, world, pixelsToMeters, entityEditorScreen.getCamera(), entityEditorScreen.getTextureSource()));

        setupProject(entityEditorScreen);

        engineJson = new AshleyEngineJson(ashleyEngine);

        objectTreeData.addCustomDataType(new PositionComponentDataDefinition(this));
        objectTreeData.addCustomDataType(new AnchorComponentDataDefinition(this));
        objectTreeData.addCustomDataType(new ScaleComponentDataDefinition(this));
        objectTreeData.addCustomDataType(new FacingComponentDataDefinition(this));
        objectTreeData.addCustomDataType(new SpriteComponentDataDefinition(this));
        objectTreeData.addCustomDataType(new SpriteStateComponentDataDefinition(this));
        objectTreeData.addCustomDataType(createSpriteStateDataType());

        objectTreeData.addCustomDataType(new Box2DBodyComponentDataDefinition(this));
        objectTreeData.addCustomDataType(createFixtureDefinitionDataType());
        objectTreeData.addCustomDataType(createFixtureShapeDataType());

        if (project.hasChild("customDataDefinitions")) {
            for (JsonValue customDataDefinition : project.get("customDataDefinitions")) {
                String id = customDataDefinition.getString("id");
                String name = customDataDefinition.getString("name");
                String className = customDataDefinition.getString("className");
                boolean component = customDataDefinition.getBoolean("component");

                CustomDataDefinition dataDefinition = new CustomDataDefinition(objectTreeData, id, component, name, className);

                for (JsonValue field : customDataDefinition.get("fields")) {
                    String fieldName = field.getString("name");
                    FieldDefinition.Type type = FieldDefinition.Type.valueOf(field.getString("type"));
                    String fieldTypeId = field.getString("fieldTypeId");
                    dataDefinition.addFieldType(fieldName, type, fieldTypeId);
                }

                objectTreeData.addCustomDataType(dataDefinition);
            }
        }

        if (project.hasChild("templates")) {
            for (JsonValue template : project.get("templates")) {
                String path = template.getString("path", null);
                JsonValue data = template.get("data");
                AshleyEntityDefinition entityDefinition = new AshleyEntityDefinition(engineJson, objectTreeData, data);
                objectTreeData.addTemplate(path, entityDefinition.getName(), entityDefinition);
            }
        }

        if (project.hasChild("entityGroups")) {
            for (JsonValue entityGroup : project.get("entityGroups")) {
                String name = entityGroup.getString("name");
                boolean enabled = entityGroup.getBoolean("enabled", true);
                objectTreeData.addEntityGroup(name, enabled);
                for (JsonValue entity : entityGroup.get("entities")) {
                    String path = entity.getString("path", null);
                    Entity ashleyEntity = ashleyEngine.createEntity();
                    JsonValue data = entity.get("data");
                    AshleyEntityDefinition entityDefinition = new AshleyEntityDefinition(engineJson, objectTreeData, data, ashleyEntity);
                    initializeAshleyEntity(ashleyEntity, entityDefinition);
                    objectTreeData.addEntity(name, path, entityDefinition.getName(), entityDefinition);
                    if (enabled)
                        ashleyEngine.addEntity(ashleyEntity);
                }
            }
        }
    }

    private DataDefinition createFixtureShapeDataType() {
        CustomClassDataDefinition fixtureDefinitionDef = new CustomClassDataDefinition("FixtureShape", false, "Fixture shape", FixtureShape.class);
        return fixtureDefinitionDef;
    }

    private DataDefinition createFixtureDefinitionDataType() {
        CustomClassDataDefinition fixtureDefinitionDef = new CustomClassDataDefinition("FixtureDefinition", false, "Fixture definition", FixtureDefinition.class);
        fixtureDefinitionDef.addFieldType("category", FieldDefinition.Type.Array, StringComponentFieldType.ID);
        fixtureDefinitionDef.addFieldType("mask", FieldDefinition.Type.Array, StringComponentFieldType.ID);
        fixtureDefinitionDef.addFieldType("sensorName", StringComponentFieldType.ID);
        fixtureDefinitionDef.addFieldType("sensorType", StringComponentFieldType.ID);
        fixtureDefinitionDef.addFieldType("sensor", BooleanComponentFieldType.ID);
        fixtureDefinitionDef.addFieldType("friction", FloatComponentFieldType.ID);
        fixtureDefinitionDef.addFieldType("restitution", FloatComponentFieldType.ID);
        fixtureDefinitionDef.addFieldType("density", FloatComponentFieldType.ID);
        fixtureDefinitionDef.addFieldType("shape", "FixtureShape");
        return fixtureDefinitionDef;
    }

    private DataDefinition createSpriteStateDataType() {
        CustomClassDataDefinition spriteStateDataDef = new CustomClassDataDefinition("SpriteStateDataDef", false, "Sprite state data", SpriteStateDataDef.class);
        spriteStateDataDef.addFieldType("width", FloatComponentFieldType.ID);
        spriteStateDataDef.addFieldType("height", FloatComponentFieldType.ID);
        spriteStateDataDef.addFieldType("properties", GraphSpritesPropertiesFieldType.ID);
        return spriteStateDataDef;
    }

    @Override
    public FileHandle getProjectFolder() {
        return folder;
    }

    private void setupProject(EntityEditorScreen<Component, AshleyEntityDefinition> entityEditorScreen) {
        FileHandle child = folder.child(settings.getRendererPipeline());
        try {
            InputStream inputStream = child.read();
            try {
                PipelineRenderer pipelineRenderer = GraphLoader.loadGraph(inputStream, new PipelineLoaderCallback(timeKeeper));
                // TODO temporary camera
                pipelineRenderer.setPipelineProperty("Camera", entityEditorScreen.getCamera());

                UpdatingRenderingSystem renderingSystem = new UpdatingRenderingSystem(0, timeKeeper, pipelineRenderer, directTextureLoader);
                ashleyEngine.addSystem(renderingSystem);

                UpdatingBox2DSystem physicsSystem = new UpdatingBox2DSystem(1, world, pixelsToMeters);
                ashleyEngine.addSystem(physicsSystem);

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
    }

    @Override
    public void save() {
        JsonValue project = new JsonValue(JsonValue.ValueType.object);

        JsonValue settingsValue = new JsonValue(JsonValue.ValueType.object);
        settingsValue.addChild("rendererPipeline", new JsonValue(settings.getRendererPipeline()));
        settingsValue.addChild("assetsFolder", new JsonValue(settings.getAssetsFolder()));
        settingsValue.addChild("exportFolder", new JsonValue(settings.getExportFolder()));
        settingsValue.addChild("templatesSubfolder", new JsonValue(settings.getTemplatesSubfolder()));
        settingsValue.addChild("entitiesSubfolder", new JsonValue(settings.getEntitiesSubfolder()));

        project.addChild("settings", settingsValue);

        JsonValue entityGroups = new JsonValue(JsonValue.ValueType.array);
        for (String entityGroup : objectTreeData.getEntityGroups()) {
            JsonValue group = new JsonValue(JsonValue.ValueType.object);
            group.addChild("name", new JsonValue(entityGroup));
            group.addChild("enabled", new JsonValue(objectTreeData.isEntityGroupEnabled(entityGroup)));

            JsonValue entities = new JsonValue(JsonValue.ValueType.array);
            for (ObjectTreeData.LocatedEntityDefinition<AshleyEntityDefinition> entity : objectTreeData.getEntities(entityGroup)) {
                JsonValue entityJson = new JsonValue(JsonValue.ValueType.object);
                entityJson.addChild("path", new JsonValue(entity.getPath()));
                entityJson.addChild("data", entity.getEntityDefinition().toJson());
                entities.addChild(entityJson);
            }
            group.addChild("entities", entities);

            entityGroups.addChild(group);
        }
        project.addChild("entityGroups", entityGroups);

        JsonValue customDataDefinitions = new JsonValue(JsonValue.ValueType.array);
        for (DataDefinition<?, ?> dataDefinition : objectTreeData.getDataDefinitions()) {
            if (dataDefinition.isStoredWithProject()) {
                JsonValue customComponentJson = new JsonValue(JsonValue.ValueType.object);
                customComponentJson.addChild("id", new JsonValue(dataDefinition.getId()));
                customComponentJson.addChild("name", new JsonValue(dataDefinition.getName()));
                customComponentJson.addChild("className", new JsonValue(dataDefinition.getClassName()));
                customComponentJson.addChild("component", new JsonValue(dataDefinition.isComponent()));
                JsonValue fields = new JsonValue(JsonValue.ValueType.array);
                for (FieldDefinition fieldDefinition : dataDefinition.getFieldTypes()) {
                    JsonValue field = new JsonValue(JsonValue.ValueType.object);
                    field.addChild("name", new JsonValue(fieldDefinition.getName()));
                    field.addChild("type", new JsonValue(fieldDefinition.getType().name()));
                    field.addChild("fieldTypeId", new JsonValue(fieldDefinition.getFieldTypeId()));
                    fields.addChild(field);
                }
                customComponentJson.addChild("fields", fields);

                customDataDefinitions.addChild(customComponentJson);
            }
        }
        project.addChild("customDataDefinitions", customDataDefinitions);

        JsonValue templates = new JsonValue(JsonValue.ValueType.array);
        for (ObjectTreeData.LocatedEntityDefinition<AshleyEntityDefinition> template : objectTreeData.getTemplates()) {
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

    private void createSettings(JsonValue project) {
        JsonValue settings = project.get("settings");
        Runnable exportRunnable = this::exportProject;

        if (settings != null) {
            String rendererPipeline = settings.getString("rendererPipeline", null);
            String assetsFolder = settings.getString("assetsFolder", null);
            String exportFolder = settings.getString("exportFolder", null);
            String templatesSubfolder = settings.getString("templatesSubfolder", null);
            String entitiesSubfolder = settings.getString("entitiesSubfolder", null);

            this.settings = new AshleyGraphSettings(
                    rendererPipeline, assetsFolder, exportFolder, templatesSubfolder, entitiesSubfolder, exportRunnable);
        } else {
            this.settings = new AshleyGraphSettings(
                    "assets/pipeline.json", "assets",
                    "assets",
                    "templates", "entities",
                    exportRunnable);
        }
    }

    private void exportProject() {
        FileHandle exportFolder = folder.child(settings.getExportFolder());
        String templatesSubfolder = settings.getTemplatesSubfolder();
        FileHandle templatesFolder = exportFolder.child(templatesSubfolder);
        FileHandle entitiesFolder = exportFolder.child(settings.getEntitiesSubfolder());

        templatesFolder.emptyDirectory();
        entitiesFolder.emptyDirectory();

        for (ObjectTreeData.LocatedEntityDefinition<AshleyEntityDefinition> template : objectTreeData.getTemplates()) {
            String path = template.getPath();
            AshleyEntityDefinition entityDefinition = template.getEntityDefinition();
            String fullPath = ObjectTree.getFullPath(path, entityDefinition.getName() + ".json");

            FileHandle templateFile = templatesFolder.child(fullPath);
            JsonValue jsonValue = entityDefinition.exportJson(templatesSubfolder);
            templateFile.writeString(jsonValue.toJson(JsonWriter.OutputType.json), false);
        }

        for (String entityGroup : objectTreeData.getEntityGroups()) {
            FileHandle entityGroupFile = entitiesFolder.child(entityGroup + ".json");

            JsonValue json = new JsonValue(JsonValue.ValueType.object);
            JsonValue entityArray = new JsonValue(JsonValue.ValueType.array);
            for (ObjectTreeData.LocatedEntityDefinition<AshleyEntityDefinition> entity : objectTreeData.getEntities(entityGroup)) {
                AshleyEntityDefinition entityDefinition = entity.getEntityDefinition();
                entityArray.addChild(entityDefinition.exportJson(templatesSubfolder));
            }

            json.addChild("entities", entityArray);

            entityGroupFile.writeString(json.toJson(JsonWriter.OutputType.json), false);
        }
    }

    @Override
    public EntityGroup createEntityGroup(String entityGroupName, boolean enabled) {
        return new DefaultEntityGroup(entityGroupName, enabled);
    }

    @Override
    public EntityGroupFolder createEntityFolder(String name) {
        return new DefaultEntityGroupFolder(name);
    }

    @Override
    public AshleyEntityDefinition createEntity(String id, String name, boolean enabled) {
        Entity entity = ashleyEngine.createEntity();
        if (enabled)
            ashleyEngine.addEntity(entity);

        AshleyEntityDefinition ashleyEntityDefinition = new AshleyEntityDefinition(id, name, objectTreeData, entity);
        initializeAshleyEntity(entity, ashleyEntityDefinition);
        return ashleyEntityDefinition;
    }

    @Override
    public void setEntityEnabled(AshleyEntityDefinition entity, boolean enabled) {
        if (enabled)
            ashleyEngine.addEntity(entity.getEntity());
        else
            ashleyEngine.removeEntity(entity.getEntity());
    }

    private void initializeAshleyEntity(Entity entity, AshleyEntityDefinition ashleyEntityDefinition) {
        entity.add(new AshleyEntityComponent(ashleyEntityDefinition));
    }

    @Override
    public EntityTemplatesFolder createTemplatesFolder(String name) {
        return new DefaultEntityTemplatesFolder(name);
    }

    @Override
    public AshleyEntityDefinition createTemplate(String id, String name) {
        return new AshleyEntityDefinition(id, name, objectTreeData);
    }

    @Override
    public AshleyEntityDefinition convertToTemplate(String id, String name, AshleyEntityDefinition entity) {
        AshleyEntityDefinition template = new AshleyEntityDefinition(engineJson, objectTreeData, entity.toJson());
        template.setId(id);
        template.setName(name);

        for (String componentId : entity.getComponents()) {
            entity.removeComponent(componentId);
        }
        for (String entityTemplate : entity.getTemplates()) {
            entity.removeTemplate(entityTemplate);
        }
        entity.addTemplate(id);

        return template;
    }

    @Override
    public void removeTemplate(AshleyEntityDefinition value) {
        // Don't have to do anything
    }

    @Override
    public void removeEntity(AshleyEntityDefinition value) {
        ashleyEngine.removeEntity(value.getEntity());
    }

    @Override
    public boolean supportsComponent(Class<? extends Component> componentClass) {
        return ClassReflection.isAssignableFrom(Component.class, componentClass);
    }

    @Override
    public Component createCoreComponent(Class<? extends Component> coreComponent) {
        return ashleyEngine.createComponent(coreComponent);
    }

    @Override
    public void entityChanged(AshleyEntityDefinition entityDefinition) {
        if (entityDefinition.isEntity()) {
            Entity entity = entityDefinition.getEntity();
            AshleyEntityComponent ashleyEntityComponent = entity.getComponent(AshleyEntityComponent.class);
            ashleyEntityComponent.setDirty(true);
        }

        entityDefinition.rebuildEntity();

        entityInspector.entityUpdated();
    }

    @Override
    public void dispose() {
        for (EntitySystem system : ashleyEngine.getSystems()) {
            if (system instanceof Disposable)
                ((Disposable) system).dispose();
        }

        if (graphPreviewRenderer != null) {
            graphPreviewRenderer.dispose();
        }
        directTextureLoader.dispose();
        whitePixel.dispose();
        CommonShaderConfiguration.setDefaultTextureRegionProperty(null);
    }
}
