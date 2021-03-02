package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.component.DataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.DataStorage;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityDefinition;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data.ComponentDataStorage;

public class AshleyEntityDefinition extends DefaultEntityDefinition<Component> {
    private ObjectTreeData<AshleyEntityDefinition> objectTreeData;
    private Entity entity;
    private ObjectMap<String, DataStorage> components = new ObjectMap<>();
    private ObjectMap<String, DataStorage> inheritedComponents = new ObjectMap<>();
    private Array<String> templates = new Array<>();

    public AshleyEntityDefinition(Json json, ObjectTreeData<AshleyEntityDefinition> objectTreeData, JsonValue value) {
        this(json, objectTreeData, value, null);
    }

    public AshleyEntityDefinition(Json json, ObjectTreeData<AshleyEntityDefinition> objectTreeData, JsonValue value, Entity entity) {
        super(value.getString("id"), value.getString("name"), entity != null);
        this.objectTreeData = objectTreeData;
        this.entity = entity;
        templates.addAll(value.get("templates").asStringArray());
        for (JsonValue coreComponent : value.get("components")) {
            String id = coreComponent.getString("id");
            JsonValue data = coreComponent.get("data");
            DataDefinition<?> dataDefinition = objectTreeData.getDataDefinitionById(id);
            DataStorage dataStorage = dataDefinition.loadDataStorage(json, data);
            components.put(id, dataStorage);
        }

        rebuildEntity();
    }

    public AshleyEntityDefinition(String id, String name, ObjectTreeData<AshleyEntityDefinition> objectTreeData) {
        this(id, name, objectTreeData, null);
    }

    public AshleyEntityDefinition(String id, String name, ObjectTreeData<AshleyEntityDefinition> objectTreeData, Entity entity) {
        super(id, name, entity != null);
        this.objectTreeData = objectTreeData;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public void rebuildEntity() {
        if (isEntity()) {
            for (ObjectMap.Entry<String, DataStorage> inheritedComponent : inheritedComponents) {
                if (inheritedComponent.value instanceof ComponentDataStorage) {
                    entity.remove(((ComponentDataStorage) inheritedComponent.value).getComponent().getClass());
                }
            }
            inheritedComponents.clear();

            for (ObjectMap.Entry<String, DataStorage> component : components) {
                if (component.value instanceof ComponentDataStorage) {
                    entity.remove(((ComponentDataStorage) component.value).getComponent().getClass());
                }
            }

            for (String inheritedComponentId : getInheritedComponents()) {
                if (!hasComponent(inheritedComponentId)) {
                    DataStorage inheritedCoreComponent = getInheritedComponent(inheritedComponentId);
                    inheritedComponents.put(inheritedComponentId, inheritedCoreComponent);
                    if (inheritedCoreComponent instanceof ComponentDataStorage) {
                        ComponentDataStorage componentDataStorage = ((ComponentDataStorage) inheritedCoreComponent).createCopy();
                        entity.add(componentDataStorage.getComponent());
                    }
                }
            }

            for (ObjectMap.Entry<String, DataStorage> component : components) {
                if (component.value instanceof ComponentDataStorage) {
                    entity.add(((ComponentDataStorage) component.value).getComponent());
                }
            }
        }
    }

    @Override
    public Iterable<String> getTemplates() {
        return new Array.ArrayIterable<>(templates);
    }

    @Override
    public boolean hasTemplate(String id) {
        return templates.contains(id, false);
    }

    @Override
    public void addTemplate(String id) {
        templates.add(id);
    }

    @Override
    public void removeTemplate(String id) {
        templates.removeValue(id, false);
    }

    @Override
    public Iterable<String> getInheritedComponents() {
        ObjectSet<String> result = new ObjectSet<>();
        for (String template : templates) {
            AshleyEntityDefinition templateDefinition = objectTreeData.getTemplateById(template).getEntityDefinition();
            for (String componentId : templateDefinition.getInheritedComponents()) {
                result.add(componentId);
            }
            for (String componentId : templateDefinition.getComponents()) {
                result.add(componentId);
            }
        }

        return result;
    }

    @Override
    public void addComponent(String componentId, DataStorage dataStorage) {
        components.put(componentId, dataStorage);
        if (isEntity() && dataStorage instanceof ComponentDataStorage)
            entity.add(((ComponentDataStorage) dataStorage).getComponent());
    }

    @Override
    public void removeComponent(String componentId) {
        DataStorage dataStorage = components.remove(componentId);
        if (isEntity() && dataStorage instanceof ComponentDataStorage)
            entity.remove(((ComponentDataStorage) dataStorage).getComponent().getClass());
    }

    @Override
    public Iterable<String> getComponents() {
        return new ObjectMap.Keys<>(components);
    }

    @Override
    public boolean hasComponent(String componentId) {
        return components.containsKey(componentId);
    }

    @Override
    public DataStorage getComponent(String componentId) {
        return components.get(componentId);
    }

    private DataStorage resolveComponent(AshleyEntityDefinition entityDefinition, String componentId) {
        DataStorage result = entityDefinition.getComponent(componentId);
        if (result != null)
            return result;
        return entityDefinition.getInheritedComponent(componentId);
    }

    @Override
    public DataStorage getInheritedComponent(String componentId) {
        DataStorage result = null;
        for (String template : templates) {
            AshleyEntityDefinition templateDefinition = (AshleyEntityDefinition) objectTreeData.getTemplateById(template).getEntityDefinition();
            DataStorage component = resolveComponent(templateDefinition, componentId);
            if (component != null)
                result = component;
        }
        return result;
    }

    public JsonValue toJson() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        result.addChild("id", new JsonValue(getId()));
        result.addChild("name", new JsonValue(getName()));

        JsonValue templates = new JsonValue(JsonValue.ValueType.array);
        for (String template : this.templates) {
            templates.addChild(new JsonValue(template));
        }
        result.addChild("templates", templates);

        JsonValue components = new JsonValue(JsonValue.ValueType.array);
        for (ObjectMap.Entry<String, DataStorage> componentEntry : this.components) {
            String id = componentEntry.key;
            DataDefinition dataDefinition = objectTreeData.getDataDefinitionById(id);

            JsonValue component = new JsonValue(JsonValue.ValueType.object);
            component.addChild("id", new JsonValue(id));
            component.addChild("data", dataDefinition.serializeDataStorage(json, componentEntry.value));
            components.addChild(component);
        }
        result.addChild("components", components);

        return result;
    }

    private Component createComponentCopy(Component coreComponent) {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        return json.fromJson(Component.class, json.toJson(coreComponent, Component.class));
    }

    public JsonValue exportJson(String templateFolder) {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        JsonValue templateArray = new JsonValue(JsonValue.ValueType.array);
        for (String templateId : this.templates) {
            ObjectTreeData.LocatedEntityDefinition<AshleyEntityDefinition> template = objectTreeData.getTemplateById(templateId);
            String path = templateFolder + "/" + template.getPath() + ".json";
            templateArray.addChild(new JsonValue(path));
        }
        result.addChild("tpl:extends", templateArray);

        for (ObjectMap.Entry<String, DataStorage> component : components) {
            DataDefinition dataDefinition = objectTreeData.getDataDefinitionById(component.key);
            String className = dataDefinition.getClassName();
            JsonValue componentJson = dataDefinition.exportComponent(json, component.value);
            result.addChild(className, componentJson);
        }

        return result;
    }
}
