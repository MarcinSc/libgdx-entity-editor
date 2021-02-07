package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component;

import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.def.SpriteStateDataDef;

public class SpriteStateComponent extends DirtyComponent {
    private String state;
    private String timePropertyName;
    private ObjectMap<String, SpriteStateDataDef> states;

    public String getState() {
        return state;
    }

    public String getTimePropertyName() {
        return timePropertyName;
    }

    public void setState(String state) {
        this.state = state;
        setDirty();
    }

    public ObjectMap<String, SpriteStateDataDef> getStates() {
        return states;
    }
}
