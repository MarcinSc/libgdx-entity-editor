package com.gempukku.libgdx.entity.editor.data;

import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityGroupsNode extends ObjectTreeNode<EntityGroupNode, Object> {
    public EntityGroupsNode(String text) {
        super(new VisLabel(text));
    }
}
