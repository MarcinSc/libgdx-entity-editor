package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.gempukku.libgdx.entity.editor.data.component.CustomComponentDefinition;
import com.gempukku.libgdx.entity.editor.data.impl.DefaultEntityDefinition;

public class AshleyEntityDefinition extends DefaultEntityDefinition<Component> {
    private Entity entity;

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
        JsonValue result = new JsonValue(JsonValue.ValueType.object);
        result.addChild("name", new JsonValue(getName()));

        JsonValue coreComponents = new JsonValue(JsonValue.ValueType.array);
        for (Component component : entity.getComponents()) {
            Json json = new Json(JsonWriter.OutputType.json);
            JsonValue componentJson = new JsonReader().parse(json.toJson(component, Component.class));
            componentJson.remove("dirty");
            coreComponents.addChild(componentJson);
        }
        result.addChild("coreComponents", coreComponents);

        return result;
    }
}
