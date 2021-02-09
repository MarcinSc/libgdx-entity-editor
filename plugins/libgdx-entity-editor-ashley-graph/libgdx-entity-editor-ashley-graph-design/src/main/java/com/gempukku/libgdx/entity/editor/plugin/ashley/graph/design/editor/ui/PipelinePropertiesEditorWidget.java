package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ObjectMap;

public class PipelinePropertiesEditorWidget extends Table {
    public PipelinePropertiesEditorWidget(
            Skin skin,
            String label, ObjectMap<String, Object> pipelineProperties, Callback callback) {

    }

    public interface Callback {
        void setValue(ObjectMap<String, Object> value);
    }
}
