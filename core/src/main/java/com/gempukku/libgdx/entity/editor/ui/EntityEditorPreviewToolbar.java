package com.gempukku.libgdx.entity.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.kotcrab.vis.ui.util.Validators;
import com.kotcrab.vis.ui.widget.VisCheckBox;
import com.kotcrab.vis.ui.widget.VisSelectBox;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisValidatableTextField;

public class EntityEditorPreviewToolbar extends VisTable {
    private final VisSelectBox<EntityEditorPreviewZoom> zoomSelectBox;
    private VisCheckBox snapEnabledCheckBox;
    private VisValidatableTextField snapTextField;

    public EntityEditorPreviewToolbar() {
        pad(2);
        left();
        zoomSelectBox = new VisSelectBox<>();
        zoomSelectBox.setItems(EntityEditorPreviewZoom.values());
        zoomSelectBox.setSelected(EntityEditorPreviewZoom.Z10);

        snapEnabledCheckBox = new VisCheckBox("Snap ");
        snapEnabledCheckBox.setChecked(true);
        snapEnabledCheckBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        snapTextField.setDisabled(!snapEnabledCheckBox.isChecked());
                    }
                });

        snapTextField = new VisValidatableTextField(Validators.FLOATS, new Validators.GreaterThanValidator(0f));
        snapTextField.setRestoreLastValid(true);
        snapTextField.setText("1");
        snapTextField.setAlignment(Align.right);

        add("Zoom: ");
        add(zoomSelectBox);
        addSeparator(true).pad(3);
        add(snapEnabledCheckBox);
        add(snapTextField).width(30);
        addSeparator(true).pad(3);
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

    public float getSnap() {
        if (!snapEnabledCheckBox.isChecked())
            return 0;
        return Float.parseFloat(snapTextField.getText());
    }

    public void addZoomListener(EventListener listener) {
        zoomSelectBox.addListener(listener);
    }

    public void removeZoomListener(EventListener listener) {
        zoomSelectBox.removeListener(listener);
    }
}
