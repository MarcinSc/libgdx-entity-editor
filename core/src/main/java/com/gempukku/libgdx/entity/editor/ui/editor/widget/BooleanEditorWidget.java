package com.gempukku.libgdx.entity.editor.ui.editor.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class BooleanEditorWidget extends VisTable {
    private VisCheckBox checkBox;

    public BooleanEditorWidget(
            float width, boolean editable,
            String label, boolean value,
            BooleanEditorWidget.Callback callback) {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.update(checkBox.isChecked());
            }
        };

        checkBox = new VisCheckBox(label, value);
        checkBox.align(Align.left);
        checkBox.addListener(changeListener);
        checkBox.setDisabled(!editable);

        add(checkBox).growX().row();
    }

    public interface Callback {
        void update(boolean value);
    }
}
