package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.component.CustomDataDefinition;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityDefinition;

public class AshleyEntityDefinition extends DefaultEntityDefinition<Component> {
    private ObjectTreeData<AshleyEntityDefinition> objectTreeData;
    private Entity entity;
    private ObjectMap<Class<? extends Component>, Component> coreComponents = new ObjectMap<>();
    private ObjectMap<Class<? extends Component>, Component> inheritedCoreComponents = new ObjectMap<>();
    private ObjectMap<String, ObjectMap<String, Object>> customComponents = new ObjectMap<>();
    private Array<String> templates = new Array<>();

    public AshleyEntityDefinition(Json json, ObjectTreeData<AshleyEntityDefinition> objectTreeData, JsonValue value) {
        this(json, objectTreeData, value, null);
    }

    public AshleyEntityDefinition(Json json, ObjectTreeData<AshleyEntityDefinition> objectTreeData, JsonValue value, Entity entity) {
        super(value.getString("id"), value.getString("name"), entity != null);
        this.objectTreeData = objectTreeData;
        this.entity = entity;
        templates.addAll(value.get("templates").asStringArray());
        for (JsonValue coreComponent : value.get("coreComponents")) {
            Component component = json.readValue(Component.class, coreComponent);
            coreComponents.put(component.getClass(), component);
        }
        for (JsonValue customComponent : value.get("customComponents")) {
            String id = customComponent.getString("id");
            JsonValue data = customComponent.get("data");
            ObjectMap<String, Object> componentData = json.readValue(ObjectMap.class, data);
            customComponents.put(id, componentData);
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
            for (Class<? extends Component> inheritedCoreComponentClass : inheritedCoreComponents.keys()) {
                entity.remove(inheritedCoreComponentClass);
            }
            inheritedCoreComponents.clear();

            for (Class<? extends Component> coreComponent : coreComponents.keys()) {
                entity.remove(coreComponent);
            }

            for (Class<? extends Component> inheritedCoreComponentClass : getInheritedCoreComponents()) {
                if (!hasCoreComponent(inheritedCoreComponentClass)) {
                    Component inheritedCoreComponent = getInheritedCoreComponent(inheritedCoreComponentClass);
                    Component componentCopy = createComponentCopy(inheritedCoreComponent);
                    inheritedCoreComponents.put(inheritedCoreComponentClass, componentCopy);
                    entity.add(componentCopy);
                }
            }

            for (ObjectMap.Entry<Class<? extends Component>, Component> coreComponentEntry : coreComponents) {
                entity.add(coreComponentEntry.value);
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
    public Iterable<Class<? extends Component>> getInheritedCoreComponents() {
        Array<Class<? extends Component>> result = new Array<>();
        for (String template : templates) {
            AshleyEntityDefinition templateDefinition = objectTreeData.getTemplateById(template).getEntityDefinition();
            for (Class<? extends Component> inheritedCoreComponent : templateDefinition.getInheritedCoreComponents()) {
                result.add(inheritedCoreComponent);
            }
            for (Class<? extends Component> coreComponent : templateDefinition.getCoreComponents()) {
                result.add(coreComponent);
            }
        }

        return result;
    }

    @Override
    public void addCoreComponent(Component coreComponent) {
        coreComponents.put(coreComponent.getClass(), coreComponent);
        if (isEntity())
            entity.add(coreComponent);
    }

    @Override
    public void removeCoreComponent(Class<? extends Component> coreComponent) {
        if (isEntity())
            entity.remove(coreComponent);
        coreComponents.remove(coreComponent);
    }

    @Override
    public Iterable<Class<? extends Component>> getCoreComponents() {
        return new ObjectMap.Keys<>(coreComponents);
    }

    @Override
    public boolean hasCoreComponent(Class<? extends Component> coreComponent) {
        return coreComponents.containsKey(coreComponent);
    }

    @Override
    public Component getCoreComponent(Class<? extends Component> clazz) {
        return coreComponents.get(clazz);
    }

    private Component resolveCoreComponent(AshleyEntityDefinition entityDefinition, Class<? extends Component> coreComponentClass) {
        Component result = entityDefinition.getCoreComponent(coreComponentClass);
        if (result != null)
            return result;
        return entityDefinition.getInheritedCoreComponent(coreComponentClass);
    }

    @Override
    public Component getInheritedCoreComponent(Class<? extends Component> coreComponentClass) {
        Component result = null;
        for (String template : templates) {
            AshleyEntityDefinition templateDefinition = (AshleyEntityDefinition) objectTreeData.getTemplateById(template).getEntityDefinition();
            Component component = resolveCoreComponent(templateDefinition, coreComponentClass);
            if (component != null)
                result = component;
        }
        return result;
    }

    @Override
    public boolean hasCustomComponent(String id) {
        return customComponents.containsKey(id);
    }

    @Override
    public void addCustomComponent(String id, ObjectMap<String, Object> componentData) {
        customComponents.put(id, componentData);
    }

    @Override
    public void removeCustomComponent(String id) {
        customComponents.remove(id);
    }

    @Override
    public ObjectMap<String, ObjectMap<String, Object>> getInheritedCustomComponents() {
        ObjectMap<String, ObjectMap<String, Object>> result = new ObjectMap<>();
        for (String template : templates) {
            AshleyEntityDefinition templateDefinition = objectTreeData.getTemplateById(template).getEntityDefinition();
            result.putAll(templateDefinition.getInheritedCustomComponents());
            result.putAll(templateDefinition.getCustomComponents());
        }
        return result;
    }

    @Override
    public ObjectMap<String, ObjectMap<String, Object>> getCustomComponents() {
        return customComponents;
    }

    public JsonValue toJson() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        JsonReader jsonReader = new JsonReader();

        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        result.addChild("id", new JsonValue(getId()));
        result.addChild("name", new JsonValue(getName()));

        JsonValue templates = new JsonValue(JsonValue.ValueType.array);
        for (String template : this.templates) {
            templates.addChild(new JsonValue(template));
        }
        result.addChild("templates", templates);

        JsonValue coreComponents = new JsonValue(JsonValue.ValueType.array);
        for (ObjectMap.Entry<Class<? extends Component>, Component> coreComponentEntry : this.coreComponents) {
            coreComponents.addChild(jsonReader.parse(json.toJson(coreComponentEntry.value, Component.class)));
        }
        result.addChild("coreComponents", coreComponents);

        JsonValue customComponents = new JsonValue(JsonValue.ValueType.array);
        for (ObjectMap.Entry<String, ObjectMap<String, Object>> customComponentEntry : this.customComponents) {
            JsonValue customComponent = new JsonValue(JsonValue.ValueType.object);
            customComponent.addChild("id", new JsonValue(customComponentEntry.key));
            customComponent.addChild("data", jsonReader.parse(json.toJson(customComponentEntry.value, ObjectMap.class)));
            customComponents.addChild(customComponent);
        }
        result.addChild("customComponents", customComponents);

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

        JsonReader jsonReader = new JsonReader();

        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        JsonValue templateArray = new JsonValue(JsonValue.ValueType.array);
        for (String templateId : this.templates) {
            ObjectTreeData.LocatedEntityDefinition<AshleyEntityDefinition> template = objectTreeData.getTemplateById(templateId);
            String path = templateFolder + "/" + template.getPath() + ".json";
            templateArray.addChild(new JsonValue(path));
        }
        result.addChild("tpl:extends", templateArray);

        for (ObjectMap.Entry<Class<? extends Component>, Component> coreComponent : coreComponents) {
            String className = coreComponent.key.getName();
            JsonValue componentJson = jsonReader.parse(json.toJson(coreComponent.value, coreComponent.key));
            result.addChild(className, componentJson);
        }
        for (ObjectMap.Entry<String, ObjectMap<String, Object>> customComponent : customComponents) {
            CustomDataDefinition customDataDefinition = objectTreeData.getCustomDataDefinitionById(customComponent.key);
            String className = customDataDefinition.getClassName();
            JsonValue componentJson = jsonReader.parse(json.toJson(customComponent.value, ObjectMap.class));
            result.addChild(className, componentJson);
        }

        return result;
    }
}
