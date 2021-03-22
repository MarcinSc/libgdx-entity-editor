package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

public class Box2DBodyDataComponent implements Component {
    private transient Body body;
    private transient Array<SensorData> sensorDataArray = new Array<>();

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public void addSensor(SensorData sensorData) {
        sensorDataArray.add(sensorData);
    }

    public SensorData getSensorDataByName(String name) {
        for (SensorData sensorDatum : sensorDataArray) {
            if (sensorDatum.getName().equals(name))
                return sensorDatum;
        }
        return null;
    }

    public static class SensorData {
        private String name;
        private String type;
        private Object data;

        public SensorData(String name, String type, Object data) {
            this.name = name;
            this.type = type;
            this.data = data;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public Object getData() {
            return data;
        }
    }
}
