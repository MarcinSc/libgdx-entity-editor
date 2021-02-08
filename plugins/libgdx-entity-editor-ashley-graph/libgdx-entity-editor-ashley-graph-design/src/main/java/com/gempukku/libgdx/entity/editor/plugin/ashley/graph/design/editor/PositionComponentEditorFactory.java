package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.TwoFloatEntryWidget;
import com.kotcrab.vis.ui.widget.Separator;

public class PositionComponentEditorFactory implements ComponentEditorFactory<PositionComponent> {
    @Override
    public ComponentEditor<PositionComponent> createComponentEditor(Skin skin, PositionComponent component) {
        return new PositionComponentEditor(skin, component);
    }

    private class PositionComponentEditor implements ComponentEditor<PositionComponent> {
        private Actor actor;
        private PositionComponent component;

        public PositionComponentEditor(Skin skin, PositionComponent component) {
            this.component = component;

            TwoFloatEntryWidget widget = new TwoFloatEntryWidget(
                    skin,
                    "X", component.getX(), "Y", component.getY(),
                    new TwoFloatEntryWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setPosition(value1, value2);
                        }
                    }
            );

            Table tbl = new Table(skin);
            tbl.add(new Separator()).growX().row();
            tbl.add("Position component").growX().pad(3).row();
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
        public PositionComponent getComponent() {
            return component;
        }
    }
}
