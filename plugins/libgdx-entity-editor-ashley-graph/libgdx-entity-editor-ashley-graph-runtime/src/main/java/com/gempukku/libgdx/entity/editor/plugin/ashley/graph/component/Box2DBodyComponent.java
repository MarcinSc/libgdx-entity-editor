package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.FixtureDefinition;

public class Box2DBodyComponent implements Component {
    private BodyDef.BodyType bodyType = BodyDef.BodyType.StaticBody;
    private boolean fixedRotation = false;
    private boolean bullet = false;
    private float linearDamping = 0;
    private float angularDamping = 0;
    private boolean allowSleep = true;
    private float gravityScale = 1;

    private Array<FixtureDefinition> fixtures = new Array<>();

    public boolean isFixedRotation() {
        return fixedRotation;
    }

    public void setFixedRotation(boolean fixedRotation) {
        this.fixedRotation = fixedRotation;
    }

    public boolean isBullet() {
        return bullet;
    }

    public void setBullet(boolean bullet) {
        this.bullet = bullet;
    }

    public BodyDef.BodyType getBodyType() {
        return bodyType;
    }

    public void setBodyType(BodyDef.BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public float getLinearDamping() {
        return linearDamping;
    }

    public void setLinearDamping(float linearDamping) {
        this.linearDamping = linearDamping;
    }

    public float getAngularDamping() {
        return angularDamping;
    }

    public void setAngularDamping(float angularDamping) {
        this.angularDamping = angularDamping;
    }

    public boolean isAllowSleep() {
        return allowSleep;
    }

    public void setAllowSleep(boolean allowSleep) {
        this.allowSleep = allowSleep;
    }

    public float getGravityScale() {
        return gravityScale;
    }

    public void setGravityScale(float gravityScale) {
        this.gravityScale = gravityScale;
    }

    public Array<FixtureDefinition> getFixtures() {
        return fixtures;
    }

    public void setFixtures(Array<FixtureDefinition> fixtures) {
        this.fixtures = fixtures;
    }
}
