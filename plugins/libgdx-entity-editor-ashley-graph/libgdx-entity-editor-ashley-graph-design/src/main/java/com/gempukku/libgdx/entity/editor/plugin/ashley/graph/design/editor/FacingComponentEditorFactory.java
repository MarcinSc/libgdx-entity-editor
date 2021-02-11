package com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditor;
import com.gempukku.libgdx.entity.editor.data.component.ComponentEditorFactory;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FaceDirection;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.component.FacingComponent;
import com.gempukku.libgdx.entity.editor.plugin.ashley.graph.design.editor.ui.EnumEditorWidget;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisTable;

public class FacingComponentEditorFactory implements ComponentEditorFactory<FacingComponent> {
    @Override
    public ComponentEditor<FacingComponent> createComponentEditor(FacingComponent component) {
        return new FacingComponentEditor(component);
    }

    private class FacingComponentEditor implements ComponentEditor<FacingComponent> {
        private Actor actor;
        private FacingComponent component;

        public FacingComponentEditor(FacingComponent component) {
            this.component = component;

            EnumEditorWidget<FaceDirection> widget = new EnumEditorWidget<FaceDirection>(
                    EditorConfig.LABEL_WIDTH,
                    FaceDirection.class, false,
                    "Facing", component.getFaceDirection(),
                    new EnumEditorWidget.Callback<FaceDirection>() {
                        @Override
                        public void update(FaceDirection value) {
                            component.setFaceDirection(value);
                        }
                    });

            VisTable tbl = new VisTable();
            tbl.add(new Separator()).growX().row();
            tbl.add("Facing component").growX().pad(3).row();
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
        public FacingComponent getComponent() {
            return component;
        }
    }
}
