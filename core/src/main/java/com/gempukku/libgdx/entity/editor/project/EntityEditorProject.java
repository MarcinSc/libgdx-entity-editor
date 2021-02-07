package com.gempukku.libgdx.entity.editor.project;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;

public interface EntityEditorProject extends Disposable {
    void save(FileHandle folder);

    void update(float delta);
}
