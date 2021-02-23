package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.AnchorComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.PairOfFloatsEditorWidget;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditor;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditorFactory;
import com.kotcrab.vis.ui.widget.VisTable;

public class AnchorComponentEditorFactory implements ComponentEditorFactory<AnchorComponent> {
    @Override
    public ComponentEditor<AnchorComponent> createComponentEditor(AnchorComponent component, Runnable callback, boolean editable) {
        return new AnchorComponentEditor(component, callback, editable);
    }

    private class AnchorComponentEditor implements ComponentEditor<AnchorComponent> {
        private Table actor;
        private AnchorComponent component;

        public AnchorComponentEditor(AnchorComponent component, Runnable callback, boolean editable) {
            this.component = component;

            PairOfFloatsEditorWidget widget = new PairOfFloatsEditorWidget(
                    EditorConfig.LABEL_WIDTH, editable,
                    "X", component.getX(), "Y", component.getY(),
                    new PairOfFloatsEditorWidget.Callback() {
                        @Override
                        public void update(float value1, float value2) {
                            component.setAnchor(value1, value2);
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
        public AnchorComponent getComponent() {
            return component;
        }
    }
}
