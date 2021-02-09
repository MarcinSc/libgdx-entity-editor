package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.entity.editor.data.component.CustomComponentDefinition;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityDefinition;
import com.gempukku.libgdx.lib.template.ashley.AshleyEngineJson;

public class AshleyEntityDefinition extends DefaultEntityDefinition<Component> {
    private Entity entity;

    public AshleyEntityDefinition(AshleyEngineJson json, Entity entity, JsonValue value) {
        super(value.getString("name"));
        this.entity = entity;
        for (JsonValue coreComponent : value.get("coreComponents")) {
            Component component = json.readValue(Component.class, coreComponent);
            entity.add(component);
        }
    }

    public AshleyEntityDefinition(String name, Entity entity) {
        super(name);
        this.entity = entity;
    }

    @Override
    public void addCoreComponent(Component coreComponent) {
        entity.add(coreComponent);
    }

    @Override
    public void removeCoreComponent(Component coreComponent) {
        entity.remove(coreComponent.getClass());
    }

    @Override
    public Iterable<? extends Component> getCoreComponents() {
        return entity.getComponents();
    }

    @Override
    public Iterable<CustomComponentDefinition> getCustomComponents() {
        // TODO
        return null;
    }

    @Override
    public boolean hasCoreComponent(Class<Component> coreComponent) {
        return entity.getComponent(coreComponent) != null;
    }

    @Override
    public JsonValue toJson() {
        Json json = new Json(JsonWriter.OutputType.json);
        json.setUsePrototypes(false);

        JsonReader jsonReader = new JsonReader();

        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        result.addChild("name", new JsonValue(getName()));

        JsonValue coreComponents = new JsonValue(JsonValue.ValueType.array);
        for (Component component : entity.getComponents()) {
            coreComponents.addChild(jsonReader.parse(json.toJson(component, Component.class)));
        }
        result.addChild("coreComponents", coreComponents);

        return result;
    }
}
