package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.CustomDataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.kotcrab.vis.ui.widget.VisTable;

public class CustomComponentEditor implements ComponentEditor {
    private VisTable actor;

    public CustomComponentEditor(
            CustomDataDefinition customDataDefinition, ObjectMap<String, Object> componentData,
            Runnable callback, boolean editable) {
        VisTable tbl = new VisTable();

        for (ObjectMap.Entry<String, String> fieldTypeEntry : customDataDefinition.getFieldTypes()) {
            ComponentFieldType fieldType = CustomFieldTypeRegistry.getComponentFieldTypeById(fieldTypeEntry.value);
            Actor widget = fieldType.createEditor(
                    120, editable, fieldTypeEntry.key, componentData.get(fieldTypeEntry.key), componentData, callback);
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
