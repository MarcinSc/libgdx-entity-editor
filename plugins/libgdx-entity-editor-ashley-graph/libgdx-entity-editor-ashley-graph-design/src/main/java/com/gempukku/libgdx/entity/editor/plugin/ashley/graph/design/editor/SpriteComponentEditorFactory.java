package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.FloatEditorWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.PairOfFloatsEditorWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.StringArrayEditorWidget;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.StringEditorWidget;
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

            PairOfFloatsEditorWidget position = new PairOfFloatsEditorWidget(
                    skin, EditorConfig.LABEL_WIDTH,
                    "Anchor X", component.getAnchorX(), "Anchor Y", component.getAnchorY(),
                    new PairOfFloatsEditorWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setAnchor(value1, value2);
                        }
                    }
            );

            StringArrayEditorWidget tags = new StringArrayEditorWidget(skin, "Tags", component.getTags(),
                    new StringArrayEditorWidget.Callback() {
                        @Override
                        public void setValue(Array<String> value) {
                            component.setTags(value);
                        }
                    });

            FloatEditorWidget layer = new FloatEditorWidget(skin, EditorConfig.LABEL_WIDTH, "Layer", component.getLayer(),
                    new FloatEditorWidget.Callback() {
                        @Override
                        public void update(float value) {
                            component.setLayer(value);
                        }
                    });

            StringEditorWidget textureProperty = new StringEditorWidget(skin, EditorConfig.LABEL_WIDTH, "Texture property", component.getTexturePropertyName(),
                    new StringEditorWidget.Callback() {
                        @Override
                        public void update(String value) {
                            component.setTexturePropertyName(value);
                        }
                    });

            Table tbl = new Table(skin);
            tbl.add(new Separator()).growX().row();
            tbl.add("Sprite component").growX().pad(3).row();
            tbl.add(position).growX().pad(3).row();
            tbl.add(layer).growX().pad(3).row();
            tbl.add(createTextureActor(skin)).growX().pad(3).row();
            tbl.add(textureProperty).growX().pad(3).row();
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

            textureTable.add("Atlas: ").width(EditorConfig.LABEL_WIDTH);
            textureTable.add(textFieldAtlas).growX().row();
            textureTable.add("Texture: ").width(EditorConfig.LABEL_WIDTH);
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
