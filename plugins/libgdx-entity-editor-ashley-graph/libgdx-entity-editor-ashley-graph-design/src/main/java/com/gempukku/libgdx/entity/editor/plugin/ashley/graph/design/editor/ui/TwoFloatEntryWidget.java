package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class TwoFloatEntryWidget extends Table {
    private final VisValidatableTextField field1;
    private final VisValidatableTextField field2;

    public TwoFloatEntryWidget(
            Skin skin,
            String label1, float value1,
            String label2, float value2,
            Callback callback) {
        super(skin);

        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (field1.isInputValid() && field2.isInputValid()) {
                    callback.update(Float.valueOf(field1.getText()), Float.valueOf(field2.getText()));
                }
            }
        };

        field1 = new VisValidatableTextField(Validators.FLOATS);
        field1.setRestoreLastValid(true);
        field1.setAlignment(Align.right);
        field1.setText(SimpleNumberFormatter.format(value1));
        field1.addListener(changeListener);

        field2 = new VisValidatableTextField(Validators.FLOATS);
        field2.setRestoreLastValid(true);
        field2.setAlignment(Align.right);
        field2.setText(SimpleNumberFormatter.format(value2));
        field2.addListener(changeListener);

        add(label1 + ": ");
        add(field1).growX().row();
        add(label2 + ": ");
        add(field2).growX().row();
    }

    public interface Callback {
        void update(float value1, float value2);
    }
}
