package com.gempukku.libgdx.entity.editor.data;

import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gempukku.libgdx.entity.editor.ui.ObjectTreeNode;
import com.kotcrab.vis.ui.widget.VisLabel;

public class EntityGroupFolderNode extends ObjectTreeNode<ObjectTreeNode, EntityGroupFolder> {
    public EntityGroupFolderNode(EntityGroupFolder folder, Drawable icon) {
        super(new VisLabel(folder.getName()));
        setValue(folder);
        setIcon(icon);
    }
}
