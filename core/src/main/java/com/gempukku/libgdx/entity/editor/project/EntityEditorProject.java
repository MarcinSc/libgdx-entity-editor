package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import com.gempukku.libgdx.entity.editor.EntityEditorScreen;
import com.gempukku.libgdx.entity.editor.data.EntityDefinition;
import com.gempukku.libgdx.entity.editor.data.EntityGroup;
import com.gempukku.libgdx.entity.editor.data.EntityGroupFolder;
import com.gempukku.libgdx.entity.editor.data.EntityTemplatesFolder;

public interface EntityEditorProject<T, U extends EntityDefinition> extends Disposable {
    void initialize(EntityEditorScreen<T, U> entityEditorScreen);

    FileHandle getProjectFolder();

    void save();

    void update(float delta);

    boolean supportsComponent(Class<? extends T> componentClass);

    T createCoreComponent(Class<? extends T> coreComponent);

    void entityChanged(U entityDefinition);


    EntityGroup createEntityGroup(String entityGroupName);

    EntityGroupFolder createEntityFolder(String name);

    U createEntity(String id, String name);

    EntityTemplatesFolder createTemplatesFolder(String name);

    U createTemplate(String id, String name);

    U convertToTemplate(String id, String name, U entity);

    void removeTemplate(U value);

    void removeEntity(U value);
}
