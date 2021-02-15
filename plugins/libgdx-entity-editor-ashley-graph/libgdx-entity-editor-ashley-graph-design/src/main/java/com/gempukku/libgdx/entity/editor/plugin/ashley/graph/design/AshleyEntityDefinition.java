package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.ObjectSet;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityDefinition;
import com.gempukku.libgdx.lib.template.ashley.AshleyEngineJson;

public class AshleyEntityDefinition extends DefaultEntityDefinition<Component> {
    private Entity entity;
    private Array<Class<? extends Component>> coreComponents = new Array<>();
    private Array<String> templates = new Array<>();

    public AshleyEntityDefinition(AshleyEngineJson json, Entity entity, JsonValue value) {
        super(value.getString("id"), value.getString("name"));
        this.entity = entity;
        templates.addAll(value.get("templates").asStringArray());
        for (JsonValue coreComponent : value.get("coreComponents")) {
            Component component = json.readValue(Component.class, coreComponent);
            addCoreComponent(component);
        }
    }

    public AshleyEntityDefinition(String id, String name, Entity entity) {
        super(id, name);
        this.entity = entity;
    }

    @Override
    public Iterable<Class<? extends Component>> getInheritedCoreComponents(ObjectTreeData treeData) {
        ObjectSet<Class<? extends Component>> result = new ObjectSet<>();
        for (String template : templates) {
            EntityDefinition<Component> templateDefinition = treeData.getTemplateById(template).getEntityDefinition();
            for (Class<? extends Component> inheritedCoreComponent : templateDefinition.getInheritedCoreComponents(treeData)) {
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
        entity.add(coreComponent);
        coreComponents.add(coreComponent.getClass());
    }

    @Override
    public void removeCoreComponent(Class<? extends Component> coreComponent) {
        coreComponents.removeValue(coreComponent, false);
        entity.remove(coreComponent);
    }

    @Override
    public Iterable<Class<? extends Component>> getCoreComponents() {
        return coreComponents;
    }

    @Override
    public boolean hasCoreComponent(Class<? extends Component> coreComponent) {
        return entity.getComponent(coreComponent) != null;
    }

    @Override
    public Component getCoreComponent(Class<? extends Component> clazz) {
        return entity.getComponent(clazz);
    }

    private Component resolveCoreComponent(ObjectTreeData treeData, AshleyEntityDefinition entityDefinition, Class<? extends Component> coreComponentClass) {
        Component result = entityDefinition.getCoreComponent(coreComponentClass);
        if (result != null)
            return result;
        return entityDefinition.getInheritedCoreComponent(treeData, coreComponentClass);
    }

    @Override
    public Component getInheritedCoreComponent(ObjectTreeData treeData, Class<? extends Component> coreComponentClass) {
        Component result = null;
        for (String template : templates) {
            AshleyEntityDefinition templateDefinition = (AshleyEntityDefinition) treeData.getTemplateById(template).getEntityDefinition();
            Component component = resolveCoreComponent(treeData, templateDefinition, coreComponentClass);
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
        for (Class<? extends Component> coreComponentClass : this.coreComponents) {
            Component component = entity.getComponent(coreComponentClass);
            coreComponents.addChild(jsonReader.parse(json.toJson(component, Component.class)));
        }
        result.addChild("coreComponents", coreComponents);

        return result;
    }
}
