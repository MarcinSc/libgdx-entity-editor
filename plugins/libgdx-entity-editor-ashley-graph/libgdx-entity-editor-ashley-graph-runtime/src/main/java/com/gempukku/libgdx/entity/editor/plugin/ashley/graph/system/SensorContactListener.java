package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.system;

import com.badlogic.gdx.physics.box2d.Fixture;

public interface SensorContactListener<T> {
    T createNewSensorValue();

    void contactBegun(T sensorData, Fixture other);

    void contactEnded(T sensorData, Fixture other);
}
