package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.data.component.ComponentFieldType;
import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.data.component.DataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.DataStorage;
import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.function.Consumer;

public class DefaultComponentEditor implements ComponentEditor {
    private VisTable actor;

    public DefaultComponentEditor(
            DataDefinition<?> dataDefinition, DataStorage componentData,
            Runnable callback, boolean editable) {
        VisTable tbl = new VisTable();

        for (FieldDefinition fieldDefinition : dataDefinition.getFieldTypes()) {
            final String name = fieldDefinition.getName();
            FieldDefinition.Type type = fieldDefinition.getType();
            String typeId = fieldDefinition.getFieldTypeId();
            ComponentFieldType fieldType = CustomFieldTypeRegistry.getComponentFieldTypeById(typeId);

            if (type == FieldDefinition.Type.Object) {
                Actor widget = fieldType.createEditor(editable, componentData.getValue(name),
                        new Consumer() {
                            @Override
                            public void accept(Object o) {
                                componentData.setValue(name, o);
                                callback.run();
                            }
                        });

                tbl.add(name + ": ").width(120).left().pad(3);
                tbl.add(widget).growX().pad(3).row();
            } else if (type == FieldDefinition.Type.Array) {
                ArrayFieldEditor arrayFieldEditor = new ArrayFieldEditor(editable, (Array) componentData.getValue(name),
                        fieldType,
                        new Consumer() {
                            @Override
                            public void accept(Object o) {
                                componentData.setValue(name, o);
                                callback.run();
                            }
                        });

                tbl.add(name + ":").colspan(2).left().pad(3).row();
                tbl.add(arrayFieldEditor).growX().pad(3).row();
            }
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
