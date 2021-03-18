package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.data;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.gempukku.libgdx.entity.editor.data.component.type.BooleanComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.type.FloatComponentFieldType;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.Box2DBodyComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.AshleyGraphProject;

public class Box2DBodyComponentDataDefinition extends ComponentDataDefinition<Box2DBodyComponent, Box2DBodyComponentDataStorage> {
    public Box2DBodyComponentDataDefinition(AshleyGraphProject ashleyGraphProject) {
        super(ashleyGraphProject, "Box2DBodyComponent", true, "Box2DBodyComponent", Box2DBodyComponent.class.getName());
        addFieldType("bodyType", BodyDef.BodyType.class.getName());
        addFieldType("fixtures", FieldDefinition.Type.Array, "FixtureDefinition");
        addFieldType("fixedRotation", BooleanComponentFieldType.ID);
        addFieldType("bullet", BooleanComponentFieldType.ID);
        addFieldType("linearDamping", FloatComponentFieldType.ID);
        addFieldType("angularDamping", FloatComponentFieldType.ID);
        addFieldType("allowSleep", BooleanComponentFieldType.ID);
        addFieldType("gravityScale", FloatComponentFieldType.ID);
    }

    @Override
    protected Box2DBodyComponentDataStorage createComponentDataStorage(Box2DBodyComponent component) {
        return new Box2DBodyComponentDataStorage(component);
    }

    @Override
    protected Class<Box2DBodyComponent> getComponentClass() {
        return Box2DBodyComponent.class;
    }
}
