package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.CustomComponentDefinition;
import com.kotcrab.vis.ui.widget.VisTable;

public class CustomComponentEditor implements ComponentEditor {
    private VisTable actor;

    public CustomComponentEditor(
            CustomComponentDefinition customComponentDefinition, ObjectMap<String, Object> componentData,
            Runnable callback, boolean editable) {
        VisTable tbl = new VisTable();

        for (ObjectMap.Entry<String, ComponentFieldType> fieldType : customComponentDefinition.getFieldTypes()) {
            Actor widget = fieldType.value.createEditor(
                    120, editable, fieldType.key, componentData.get(fieldType.key), componentData, callback);
            tbl.add(widget).growX().pad(3).row();
        }

        this.actor = tbl;
    }

    @Override
    public VisTable getActor() {
        return actor;
    }

    @Override
    public void refresh() {

    }
}
