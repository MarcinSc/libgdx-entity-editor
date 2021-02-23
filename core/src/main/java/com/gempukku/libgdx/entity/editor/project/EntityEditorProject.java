package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;

public interface EntityEditorProject<T> extends Disposable {
    void initialize(EntityEditorScreen entityEditorScreen);

    void save(FileHandle folder, ObjectTreeData objectTreeData);

    void update(float delta);

    boolean supportsComponent(Class<? extends T> componentClass);

    T createCoreComponent(Class<? extends T> coreComponent);

    Vector2 getEntityPosition(EntityDefinition<T> entityDefinition, Vector2 position);

    void entityChanged(EntityDefinition<T> entityDefinition);
}
