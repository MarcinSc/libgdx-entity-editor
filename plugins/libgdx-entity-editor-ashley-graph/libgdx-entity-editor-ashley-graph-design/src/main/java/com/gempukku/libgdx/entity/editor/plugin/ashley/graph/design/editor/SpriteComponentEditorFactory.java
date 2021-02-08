package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.OneFloatWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.PairOfFloatsWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.StringArrayWidget;
import com.kotcrab.vis.ui.widget.Separator;

public class SpriteComponentEditorFactory implements ComponentEditorFactory<SpriteComponent> {
    @Override
    public ComponentEditor createComponentEditor(Skin skin, SpriteComponent component) {
        return new SpriteComponentEditor(skin, component);
    }

    private class SpriteComponentEditor implements ComponentEditor<SpriteComponent> {
        private Actor actor;
        private SpriteComponent component;

        public SpriteComponentEditor(Skin skin, SpriteComponent component) {
            this.component = component;

            PairOfFloatsWidget position = new PairOfFloatsWidget(
                    skin,
                    "Anchor X", component.getAnchorX(), "Anchor Y", component.getAnchorY(),
                    new PairOfFloatsWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setAnchor(value1, value2);
                        }
                    }
            );

            StringArrayWidget tags = new StringArrayWidget(skin, "Tags", component.getTags(),
                    new StringArrayWidget.Callback() {
                        @Override
                        public void removeValue(String value) {
                            component.removeTag(value);
                        }

                        @Override
                        public void addValue(String value) {
                            component.addTag(value);
                        }
                    });

            OneFloatWidget layer = new OneFloatWidget(skin, "Layer", component.getLayer(),
                    new OneFloatWidget.Callback() {
                        @Override
                        public void update(float value) {
                            component.setLayer(value);
                        }
                    });

            Table tbl = new Table(skin);
            tbl.add(new Separator()).growX().row();
            tbl.add("Sprite component").growX().pad(3).row();
            tbl.add(position).growX().pad(3).row();
            tbl.add(layer).growX().pad(3).row();
            tbl.add(createTextureActor(skin)).growX().pad(3).row();
            tbl.add(tags).growX().pad(3).row();

            this.actor = tbl;
        }

        private Actor createTextureActor(Skin skin) {
            Table textureTable = new Table(skin);

            final TextField textFieldAtlas = new TextField(component.getAtlas(), skin);
            final TextField textFieldSprite = new TextField(component.getAtlas(), skin);

            ChangeListener changeListener = new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    component.setTexture(textFieldAtlas.getText(), textFieldSprite.getText());
                }
            };

            textFieldAtlas.setAlignment(Align.right);
            textFieldAtlas.addListener(changeListener);

            textFieldSprite.setAlignment(Align.right);
            textFieldSprite.addListener(changeListener);

            textureTable.add("Atlas: ");
            textureTable.add(textFieldAtlas).growX().row();
            textureTable.add("Texture: ");
            textureTable.add(textFieldSprite).growX().row();

            return textureTable;
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
