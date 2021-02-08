package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class ScaleComponentEditorFactory implements ComponentEditorFactory<ScaleComponent> {
    @Override
    public ComponentEditor<ScaleComponent> createComponentEditor(Skin skin, ScaleComponent component) {
        return new ScaleComponentEditor(skin, component);
    }

    private class ScaleComponentEditor implements ComponentEditor<ScaleComponent> {
        private final VisValidatableTextField xTextField;
        private final VisValidatableTextField yTextField;
        private Actor actor;
        private ScaleComponent component;

        public ScaleComponentEditor(Skin skin, ScaleComponent component) {
            this.component = component;

            xTextField = new VisValidatableTextField(Validators.FLOATS);
            xTextField.setAlignment(Align.right);
            xTextField.setText(SimpleNumberFormatter.format(component.getX()));
            xTextField.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (xTextField.isInputValid()) {
                                component.setScale(Float.parseFloat(xTextField.getText()), component.getY());
                            }
                        }
                    });

            yTextField = new VisValidatableTextField(Validators.FLOATS);
            yTextField.setAlignment(Align.right);
            yTextField.setText(SimpleNumberFormatter.format(component.getY()));
            yTextField.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (yTextField.isInputValid()) {
                                component.setScale(component.getX(), Float.parseFloat(yTextField.getText()));
                            }
                        }
                    });

            Table tbl = new Table(skin);
            tbl.add(new Separator()).growX().row();

            Table dataTable = new Table(skin);
            dataTable.add("Scale component").colspan(2).growX().row();
            dataTable.add("X: ");
            dataTable.add(xTextField).growX().row();
            dataTable.add("Y: ");
            dataTable.add(yTextField).growX().row();
            tbl.add(dataTable).growX().pad(3);

            this.actor = tbl;
        }

        @Override
        public Actor getActor() {
            return actor;
        }

        @Override
        public void refresh() {

        }

        @Override
        public ScaleComponent getComponent() {
            return component;
        }
    }
}
