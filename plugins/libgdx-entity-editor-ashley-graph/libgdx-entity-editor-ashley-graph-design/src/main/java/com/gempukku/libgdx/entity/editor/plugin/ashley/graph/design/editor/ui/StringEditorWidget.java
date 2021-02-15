package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class StringEditorWidget extends VisTable {
    private final VisTextField field;

    public StringEditorWidget(
            float width, boolean editable,
            String label, String value,
            StringEditorWidget.Callback callback) {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.update(field.getText());
            }
        };

        field = new VisTextField(value);
        field.setAlignment(Align.right);
        field.addListener(changeListener);
        field.setDisabled(!editable);

        add(label + ": ").width(width);
        add(field).growX().row();
    }

    public interface Callback {
        void update(String value);
    }
}
