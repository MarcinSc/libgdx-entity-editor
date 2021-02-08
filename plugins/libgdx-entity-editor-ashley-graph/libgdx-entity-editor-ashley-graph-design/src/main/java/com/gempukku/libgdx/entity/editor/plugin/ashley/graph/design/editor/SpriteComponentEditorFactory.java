package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.SpriteComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.TwoFloatEntryWidget;
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


            TwoFloatEntryWidget widget = new TwoFloatEntryWidget(
                    skin,
                    "Anchor X", component.getAnchorX(), "Anchor Y", component.getAnchorY(),
                    new TwoFloatEntryWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setAnchor(value1, value2);
                        }
                    }
            );

            Table tbl = new Table(skin);
            tbl.add(new Separator()).growX().row();
            tbl.add("Sprite component").growX().pad(3).row();
            tbl.add(widget).growX().pad(3).row();

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
