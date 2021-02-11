package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.ObjectTreeData;

public interface EntityEditorProject<T> extends Disposable {
    void initialize(Skin skin, EntityEditorScreen entityEditorScreen);

    void save(FileHandle folder, ObjectTreeData objectTreeData);

    void update(float delta);

    boolean supportsComponent(Class<? extends T> componentClass);

    T createCoreComponent(Class<? extends T> coreComponent);
}
