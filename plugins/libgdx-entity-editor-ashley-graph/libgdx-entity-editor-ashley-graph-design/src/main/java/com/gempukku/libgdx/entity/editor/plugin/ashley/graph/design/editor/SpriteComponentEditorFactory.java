package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.graph.util.SimpleNumberFormatter;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class SpriteComponentEditorFactory implements ComponentEditorFactory<SpriteComponent> {
    @Override
    public ComponentEditor createComponentEditor(Skin skin, SpriteComponent component) {
        return new SpriteComponentEditor(skin, component);
    }

    private class SpriteComponentEditor implements ComponentEditor<SpriteComponent> {
        private final VisValidatableTextField xTextField;
        private final VisValidatableTextField yTextField;
        private Actor actor;
        private SpriteComponent component;

        public SpriteComponentEditor(Skin skin, SpriteComponent component) {
            this.component = component;

            xTextField = new VisValidatableTextField(Validators.FLOATS);
            xTextField.setAlignment(Align.right);
            xTextField.setText(SimpleNumberFormatter.format(component.getAnchorX()));
            xTextField.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (xTextField.isInputValid()) {
                                component.setAnchor(Float.parseFloat(xTextField.getText()), component.getAnchorY());
                            }
                        }
                    });

            yTextField = new VisValidatableTextField(Validators.FLOATS);
            yTextField.setAlignment(Align.right);
            yTextField.setText(SimpleNumberFormatter.format(component.getAnchorY()));
            yTextField.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            if (yTextField.isInputValid()) {
                                component.setAnchor(component.getAnchorX(), Float.parseFloat(yTextField.getText()));
                            }
                        }
                    });

            Table tbl = new Table(skin);
            tbl.add(new Separator()).growX().row();
            tbl.add("Sprite component").growX().row();

            Table dataTable = new Table(skin);
            dataTable.add("Anchor X: ");
            dataTable.add(xTextField).growX().row();
            dataTable.add("Anchor Y: ");
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
        public SpriteComponent getComponent() {
            return component;
        }
    }
}
