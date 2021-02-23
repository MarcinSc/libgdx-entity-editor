package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.ScaleComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.PairOfFloatsEditorWidget;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditor;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditorFactory;
import com.kotcrab.vis.ui.widget.VisTable;

public class ScaleComponentEditorFactory implements ComponentEditorFactory<ScaleComponent> {
    @Override
    public ComponentEditor<ScaleComponent> createComponentEditor(ScaleComponent component, Runnable callback, boolean editable) {
        return new ScaleComponentEditor(component, callback, editable);
    }

    private class ScaleComponentEditor implements ComponentEditor<ScaleComponent> {
        private Table actor;
        private ScaleComponent component;

        public ScaleComponentEditor(ScaleComponent component, Runnable callback, boolean editable) {
            this.component = component;

            PairOfFloatsEditorWidget widget = new PairOfFloatsEditorWidget(
                    EditorConfig.LABEL_WIDTH, editable,
                    "X", component.getX(), "Y", component.getY(),
                    new PairOfFloatsEditorWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setScale(value1, value2);
                            callback.run();
                        }
                    });

            VisTable tbl = new VisTable();
            tbl.add(widget).growX().pad(3).row();

            this.actor = tbl;
        }

        @Override
        public Table getActor() {
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
