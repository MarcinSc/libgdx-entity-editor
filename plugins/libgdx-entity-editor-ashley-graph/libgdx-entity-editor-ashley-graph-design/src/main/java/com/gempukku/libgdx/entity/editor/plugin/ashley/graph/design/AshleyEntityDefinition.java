package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityDefinition;

public class AshleyEntityDefinition extends DefaultEntityDefinition<Component> {
    private ObjectTreeData objectTreeData;
    private Entity entity;
    private ObjectMap<Class<? extends Component>, Component> coreComponents = new ObjectMap<>();
    private ObjectMap<Class<? extends Component>, Component> inheritedCoreComponents = new ObjectMap<>();
    private Array<String> templates = new Array<>();

    public AshleyEntityDefinition(Json json, ObjectTreeData objectTreeData, Entity entity, JsonValue value) {
        super(value.getString("id"), value.getString("name"));
        this.objectTreeData = objectTreeData;
        this.entity = entity;
        templates.addAll(value.get("templates").asStringArray());
        for (JsonValue coreComponent : value.get("coreComponents")) {
            Component component = json.readValue(Component.class, coreComponent);
            coreComponents.put(component.getClass(), component);
        }
        rebuildEntity();
    }

    public AshleyEntityDefinition(String id, String name, ObjectTreeData objectTreeData, Entity entity) {
        super(id, name);
        this.objectTreeData = objectTreeData;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public void rebuildEntity() {
        clearInheritedCoreComponents();
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

    private void clearInheritedCoreComponents() {
        for (Class<? extends Component> inheritedCoreComponentClass : inheritedCoreComponents.keys()) {
            entity.remove(inheritedCoreComponentClass);
        }
        inheritedCoreComponents.clear();
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
            EntityDefinition<Component> templateDefinition = objectTreeData.getTemplateById(template).getEntityDefinition();
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
        entity.add(coreComponent);
    }

    @Override
    public void removeCoreComponent(Class<? extends Component> coreComponent) {
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
        return entity.getComponent(clazz);
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
            ObjectTreeData.LocatedEntityDefinition template = objectTreeData.getTemplateById(templateId);
            String path = templateFolder + "/" + template.getPath() + ".json";
            templateArray.addChild(new JsonValue(path));
        }
        result.addChild("tpl:extends", templateArray);

        for (ObjectMap.Entry<Class<? extends Component>, Component> coreComponent : coreComponents) {
            String className = coreComponent.key.getName();
            JsonValue componentJson = jsonReader.parse(json.toJson(coreComponent.value, coreComponent.key));
            result.addChild(className, componentJson);
        }

        return result;
    }
}
