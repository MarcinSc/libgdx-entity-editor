package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityGroupNode extends ObjectTreeNode<ObjectTreeNode, EntityGroup> {
    public EntityGroupNode(EntityGroup entityGroup, Drawable icon) {
        super(new VisLabel(entityGroup.getName()));
        setValue(entityGroup);
        setIcon(icon);
    }
}
