package com.gempukku.libgdx.entity.editor.ui.editor.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.function.Consumer;

public class FloatEditorWidget extends VisTable {
    private static NumberFormat numberFormat = new DecimalFormat("#0.#######");
    private final VisValidatableTextField field;

    public FloatEditorWidget(
            boolean editable, float value,
            Consumer<Number> callback) {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (field.isInputValid()) {
                    callback.accept(Float.parseFloat(field.getText()));
                }
            }
        };

        field = new VisValidatableTextField(Validators.FLOATS);
        field.setRestoreLastValid(true);
        field.setAlignment(Align.right);
        field.setText(numberFormat.format(value));
        field.addListener(changeListener);
        field.setDisabled(!editable);

        add(field).growX().row();
    }
}
