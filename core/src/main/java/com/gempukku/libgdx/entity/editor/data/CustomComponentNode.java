package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.entity.editor.data.component.CustomComponentDefinition;
import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class CustomComponentNode extends ObjectTreeNode<ObjectTreeNode, CustomComponentDefinition> {
    public CustomComponentNode(CustomComponentDefinition customComponentDefinition, Drawable icon) {
        super(new VisLabel(customComponentDefinition.getName()));
        setValue(customComponentDefinition);
        setIcon(icon);
    }
}
