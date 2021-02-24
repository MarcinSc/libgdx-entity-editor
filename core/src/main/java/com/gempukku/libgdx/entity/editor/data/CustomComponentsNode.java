package com.gempukku.libgdx.entity.editor.data;

import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class CustomComponentsNode extends ObjectTreeNode<CustomComponentNode, Object> {
    public CustomComponentsNode(String text) {
        super(new VisLabel(text));
    }
}
