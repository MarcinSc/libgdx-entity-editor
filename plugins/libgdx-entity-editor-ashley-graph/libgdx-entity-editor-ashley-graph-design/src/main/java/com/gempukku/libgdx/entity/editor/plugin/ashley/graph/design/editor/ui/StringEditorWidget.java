package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;

public class StringEditorWidget extends Table {
    private final TextField field;

    public StringEditorWidget(
            Skin skin, float width,
            String label, String value,
            StringEditorWidget.Callback callback) {
        super(skin);

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.update(field.getText());
            }
        };

        field = new TextField(value, skin);
        field.setAlignment(Align.right);
        field.addListener(changeListener);

        add(label + ": ").width(width);
        add(field).growX().row();
    }

    public interface Callback {
        void update(String value);
    }
}
