package com.xebisco.yield.editor;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.Comparator;

public class SimpleTreeNode extends DefaultMutableTreeNode {
    private final Comparator<? super TreeNode> comparator;

    public SimpleTreeNode(Object userObject, Comparator<? super TreeNode> comparator) {
        super(userObject);
        this.comparator = comparator;
    }

    public SimpleTreeNode(Object userObject) {
        this(userObject, null);
    }

    @Override
    public void add(MutableTreeNode newChild) {
        super.add(newChild);
        if (this.comparator != null) {
            this.children.sort(this.comparator);
        }
    }
}