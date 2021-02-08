package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.PairOfFloatsWidget;
import com.kotcrab.vis.ui.widget.Separator;

public class ScaleComponentEditorFactory implements ComponentEditorFactory<ScaleComponent> {
    @Override
    public ComponentEditor<ScaleComponent> createComponentEditor(Skin skin, ScaleComponent component) {
        return new ScaleComponentEditor(skin, component);
    }

    private class ScaleComponentEditor implements ComponentEditor<ScaleComponent> {
        private Actor actor;
        private ScaleComponent component;

        public ScaleComponentEditor(Skin skin, ScaleComponent component) {
            this.component = component;


            PairOfFloatsWidget widget = new PairOfFloatsWidget(
                    skin,
                    "X", component.getX(), "Y", component.getY(),
                    new PairOfFloatsWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setScale(value1, value2);
                        }
                    }
            );

            Table tbl = new Table(skin);
            tbl.add(new Separator()).growX().row();
            tbl.add("Scale component").growX().pad(3).row();
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
        public ScaleComponent getComponent() {
            return component;
        }
    }
}
