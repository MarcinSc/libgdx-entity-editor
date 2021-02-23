package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FacingComponent;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditor;
import com.gempukku.libgdx.entity.editor.ui.editor.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.ui.editor.widget.EnumEditorWidget;
import com.kotcrab.vis.ui.widget.VisTable;

public class FacingComponentEditorFactory implements ComponentEditorFactory<FacingComponent> {
    @Override
    public ComponentEditor<FacingComponent> createComponentEditor(FacingComponent component, Runnable callback, boolean editable) {
        return new FacingComponentEditor(component, callback, editable);
    }

    private class FacingComponentEditor implements ComponentEditor<FacingComponent> {
        private Table actor;
        private FacingComponent component;

        public FacingComponentEditor(FacingComponent component, Runnable callback, boolean editable) {
            this.component = component;

            EnumEditorWidget<FaceDirection> widget = new EnumEditorWidget<FaceDirection>(
                    EditorConfig.LABEL_WIDTH, editable,
                    FaceDirection.class, false,
                    "Facing", component.getFaceDirection(),
                    new EnumEditorWidget.Callback<FaceDirection>() {
                        @Override
                        public void update(FaceDirection value) {
                            component.setFaceDirection(value);
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
        public FacingComponent getComponent() {
            return component;
        }
    }
}
