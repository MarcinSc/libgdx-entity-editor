package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.PositionComponent;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditor;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.PairOfFloatsEditorWidget;
import com.kotcrab.vis.ui.widget.VisTable;

public class PositionComponentEditorFactory implements ComponentEditorFactory<PositionComponent> {
    @Override
    public ComponentEditor<PositionComponent> createComponentEditor(PositionComponent component, Runnable callback, boolean editable) {
        return new PositionComponentEditor(component, callback, editable);
    }

    private class PositionComponentEditor implements ComponentEditor<PositionComponent> {
        private Table actor;
        private PositionComponent component;

        public PositionComponentEditor(PositionComponent component, Runnable callback, boolean editable) {
            this.component = component;

            PairOfFloatsEditorWidget widget = new PairOfFloatsEditorWidget(
                    EditorConfig.LABEL_WIDTH, editable,
                    "X", component.getX(), "Y", component.getY(),
                    new PairOfFloatsEditorWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setPosition(value1, value2);
                            callback.run();
                        }
                    }
            );

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
        public PositionComponent getComponent() {
            return component;
        }
    }
}
