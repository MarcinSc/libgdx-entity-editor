package com.gempukku.libgdx.entity.editor.ui.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;
import com.gempukku.libgdx.entity.editor.data.component.DataDefinition;
import com.gempukku.libgdx.entity.editor.data.component.DataStorage;
import com.gempukku.libgdx.entity.editor.data.component.EditableType;

import java.util.function.Consumer;

public class DataDefinitionEditableType implements EditableType {
    private ObjectTreeData objectTreeData;
    private DataDefinition dataDefinition;

    public DataDefinitionEditableType(ObjectTreeData objectTreeData, DataDefinition<?, ?> dataDefinition) {
        this.objectTreeData = objectTreeData;
        this.dataDefinition = dataDefinition;
    }

    @Override
    public Actor createEditor(boolean editable, Object value, Consumer consumer) {
        DataStorage dataStorage = dataDefinition.wrapDataStorage(value);
        DefaultComponentEditor defaultComponentEditor = new DefaultComponentEditor(
                objectTreeData, dataDefinition, dataStorage,
                new Runnable() {
                    @Override
                    public void run() {
                        consumer.accept(dataDefinition.unpackFromDataStorage(dataStorage));
                    }
                }, editable);
        return defaultComponentEditor.getActor();
    }

    @Override
    public JsonValue convertToJson(Object value) {
        return dataDefinition.serializeDataStorage(dataDefinition.wrapDataStorage(value));
    }

    @Override
    public Object convertToValue(JsonValue json) {
        return dataDefinition.unpackFromDataStorage(dataDefinition.loadDataStorage(new Json(), json));
    }

    @Override
    public Object getDefaultValue() {
        return dataDefinition.createDefaultValue();
    }

    @Override
    public boolean isEditorSmall() {
        return false;
    }
}
