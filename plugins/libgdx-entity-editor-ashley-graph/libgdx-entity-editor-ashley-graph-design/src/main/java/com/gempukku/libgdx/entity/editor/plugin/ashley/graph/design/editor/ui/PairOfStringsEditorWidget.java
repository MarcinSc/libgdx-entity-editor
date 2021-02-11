package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;

public class PairOfStringsEditorWidget extends VisTable {
    private final VisTextField field1;
    private final VisTextField field2;

    public PairOfStringsEditorWidget(
            float width,
            String label1, String value1,
            String label2, String value2,
            Callback callback) {
        ChangeListener changeListener = new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                callback.update(field1.getText(), field2.getText());
            }
        };

        field1 = new VisTextField(value1);
        field1.setAlignment(Align.right);
        field1.addListener(changeListener);

        field2 = new VisTextField(value2);
        field2.setAlignment(Align.right);
        field2.addListener(changeListener);

        add(label1 + ": ").width(width);
        add(field1).growX().row();
        add(label2 + ": ").width(width);
        add(field2).growX().row();
    }

    public interface Callback {
        void update(String value1, String value2);
    }
}
