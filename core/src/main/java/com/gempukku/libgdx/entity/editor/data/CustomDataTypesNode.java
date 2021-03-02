package com.gempukku.libgdx.entity.editor.data;

import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class CustomDataTypesNode extends ObjectTreeNode<CustomDataDefinitionNode, Object> {
    public CustomDataTypesNode(String text) {
        super(new VisLabel(text));
    }
}
