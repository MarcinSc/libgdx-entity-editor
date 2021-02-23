package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;

public class SpriteStateComponent implements Component {
    private String state;
    private ObjectMap<String, SpriteStateDataDef> states = new ObjectMap<>();

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public ObjectMap<String, SpriteStateDataDef> getStates() {
        return states;
    }

    public void setStates(ObjectMap<String, SpriteStateDataDef> states) {
        this.states.clear();
        this.states.putAll(states);
    }
}
