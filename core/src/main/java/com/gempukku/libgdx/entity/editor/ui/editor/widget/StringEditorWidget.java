package com.gempukku.libgdx.entity.editor.ui.editor.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

import java.util.function.Consumer;

public class StringEditorWidget extends VisTable {
    private final VisTextField field;

    public StringEditorWidget(
            boolean editable, String value,
            Consumer<String> callback) {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.accept(field.getText());
            }
        };

        field = new VisTextField(value);
        field.setAlignment(Align.right);
        field.addListener(changeListener);
        field.setDisabled(!editable);

        add(field).growX().row();
    }
}
