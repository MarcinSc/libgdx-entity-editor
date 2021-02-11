package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class FloatEditorWidget extends VisTable {
    private final VisValidatableTextField field;

    public FloatEditorWidget(
            float width,
            String label, float value,
            FloatEditorWidget.Callback callback) {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (field.isInputValid()) {
                    callback.update(Float.valueOf(field.getText()));
                }
            }
        };

        field = new VisValidatableTextField(Validators.FLOATS);
        field.setRestoreLastValid(true);
        field.setAlignment(Align.right);
        field.setText(SimpleNumberFormatter.format(value));
        field.addListener(changeListener);

        add(label + ": ").width(width);
        add(field).growX().row();
    }

    public interface Callback {
        void update(float value);
    }
}
