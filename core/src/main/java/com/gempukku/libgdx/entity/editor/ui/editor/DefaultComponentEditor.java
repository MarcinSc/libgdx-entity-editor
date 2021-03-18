package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.component.CustomFieldTypeRegistry;
import com.gempukku.libgdx.entity.editor.data.component.DataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.DataStorage;
import com.gempukku.libgdx.entity.editor.data.component.EditableType;
import com.gempukku.libgdx.entity.editor.data.component.FieldDefinition;
import com.kotcrab.vis.ui.widget.VisTable;

import java.util.function.Consumer;

public class DefaultComponentEditor implements ComponentEditor {
    private VisTable actor;

    public DefaultComponentEditor(
            ObjectTreeData<?> objectTreeData,
            DataDefinition<?, ?> dataDefinition, DataStorage componentData,
            Runnable callback, boolean editable) {
        VisTable tbl = new VisTable();

        for (FieldDefinition fieldDefinition : dataDefinition.getFieldTypes()) {
            final String name = fieldDefinition.getName();
            FieldDefinition.Type type = fieldDefinition.getType();
            String typeId = fieldDefinition.getFieldTypeId();
            EditableType fieldType = getEditableType(objectTreeData, typeId);

            if (type == FieldDefinition.Type.Object) {
                Actor widget = fieldType.createEditor(editable, fieldType.convertToValue(componentData.getValue(name)),
                        new Consumer() {
                            @Override
                            public void accept(Object o) {
                                componentData.setValue(name, fieldType.convertToJson(o));
                                callback.run();
                            }
                        });

                if (fieldType.isEditorSmall()) {
                    tbl.add(name + ": ").width(120).left().top().pad(3);
                    tbl.add(widget).growX().pad(3).row();
                } else {
                    tbl.add(name + ":").colspan(2).left().pad(3).row();
                    tbl.add(widget).colspan(2).growX().pad(3).row();
                }
            } else if (type == FieldDefinition.Type.Array) {
                ArrayFieldEditor arrayFieldEditor = new ArrayFieldEditor(editable, componentData.getValue(name),
                        fieldType,
                        new Consumer() {
                            @Override
                            public void accept(Object o) {
                                JsonValue array = new JsonValue(JsonValue.ValueType.array);
                                for (Object value : (Array) o) {
                                    array.addChild(fieldType.convertToJson(value));
                                }

                                componentData.setValue(name, array);
                                callback.run();
                            }
                        });

                tbl.add(name + ":").colspan(2).left().pad(3).row();
                tbl.add(arrayFieldEditor).colspan(2).growX().pad(3).row();
            } else if (type == FieldDefinition.Type.Map) {
                MapFieldEditor mapFieldEditor = new MapFieldEditor(editable, componentData.getValue(name),
                        fieldType,
                        new Consumer() {
                            @Override
                            public void accept(Object o) {
                                JsonValue map = new JsonValue(JsonValue.ValueType.object);
                                for (ObjectMap.Entry<String, ?> mapEntry : (ObjectMap<String, ?>) o) {
                                    map.addChild(mapEntry.key, fieldType.convertToJson(mapEntry.value));
                                }

                                componentData.setValue(name, map);
                                callback.run();
                            }
                        });

                tbl.add(name + ":").colspan(2).left().pad(3).row();
                tbl.add(mapFieldEditor).colspan(2).growX().pad(3).row();
            }
        }

        this.actor = tbl;
    }

    private EditableType getEditableType(ObjectTreeData<?> objectTreeData, String typeId) {
        EditableType fieldType = CustomFieldTypeRegistry.getComponentFieldTypeById(typeId);
        if (fieldType == null) {
            fieldType = new DataDefinitionEditableType(objectTreeData, objectTreeData.getDataDefinitionById(typeId));
        }
        return fieldType;
    }

    @Override
    public VisTable getActor() {
        return actor;
    }

    @Override
    public void refresh() {

    }
}
