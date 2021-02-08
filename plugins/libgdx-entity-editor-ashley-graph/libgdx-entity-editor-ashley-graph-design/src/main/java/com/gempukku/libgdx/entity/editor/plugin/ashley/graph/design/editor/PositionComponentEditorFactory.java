package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class PositionComponentEditorFactory implements ComponentEditorFactory<PositionComponent> {
    @Override
    public ComponentEditor<PositionComponent> createComponentEditor(Skin skin, PositionComponent component) {
        return new PositionComponentEditor(skin, component);
    }

    private class PositionComponentEditor implements ComponentEditor<PositionComponent> {
        private final VisValidatableTextField xTextField;
        private final VisValidatableTextField yTextField;
        private Actor actor;
        private PositionComponent component;

        public PositionComponentEditor(Skin skin, PositionComponent component) {
            this.component = component;

            xTextField = new VisValidatableTextField(Validators.FLOATS);
            xTextField.setText(SimpleNumberFormatter.format(component.getX()));
            xTextField.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (xTextField.isInputValid()) {
                                component.setPosition(Float.parseFloat(xTextField.getText()), component.getY());
                            }
                        }
                    });

            yTextField = new VisValidatableTextField(Validators.FLOATS);
            yTextField.setText(SimpleNumberFormatter.format(component.getY()));
            yTextField.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (yTextField.isInputValid()) {
                                component.setPosition(component.getX(), Float.parseFloat(yTextField.getText()));
                            }
                        }
                    });

            Table tbl = new Table(skin);
            tbl.add("Position component").growX().row();

            Table dataTable = new Table(skin);
            dataTable.add("X: ");
            dataTable.add(xTextField).growX().row();
            dataTable.add("Y: ");
            dataTable.add(yTextField).growX().row();
            tbl.add(dataTable).growX();

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
        public PositionComponent getComponent() {
            return component;
        }
    }
}
