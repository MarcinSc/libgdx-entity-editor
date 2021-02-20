package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;

public class EntityEditorPreviewToolbar extends VisTable {
    private final VisSelectBox<EntityEditorPreviewZoom> zoomSelectBox;

    public EntityEditorPreviewToolbar() {
        left();
        zoomSelectBox = new VisSelectBox<EntityEditorPreviewZoom>();
        zoomSelectBox.setItems(EntityEditorPreviewZoom.values());
        zoomSelectBox.setSelected(EntityEditorPreviewZoom.Z10);

        add("Zoom: ");
        add(zoomSelectBox);
    }

    public void zoomIn() {
        int selectedIndex = zoomSelectBox.getSelectedIndex();
        if (selectedIndex + 1 < zoomSelectBox.getItems().size)
            zoomSelectBox.setSelectedIndex(selectedIndex + 1);
    }

    public void zoomOut() {
        int selectedIndex = zoomSelectBox.getSelectedIndex();
        if (selectedIndex > 0)
            zoomSelectBox.setSelectedIndex(selectedIndex - 1);
    }

    public EntityEditorPreviewZoom getZoom() {
        return zoomSelectBox.getSelected();
    }

    public void addZoomListener(EventListener listener) {
        zoomSelectBox.addListener(listener);
    }

    public void removeZoomListener(EventListener listener) {
        zoomSelectBox.removeListener(listener);
    }
}
