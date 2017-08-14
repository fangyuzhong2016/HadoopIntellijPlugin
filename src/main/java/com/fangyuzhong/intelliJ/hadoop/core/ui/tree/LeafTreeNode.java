package com.fangyuzhong.intelliJ.hadoop.core.ui.tree;

import javax.swing.tree.TreeNode;
import java.util.Enumeration;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class LeafTreeNode
        implements TreeNode
{
    private TreeNode parent;
    private Object userObject;

    public LeafTreeNode(TreeNode parent, Object userObject)
    {
        this.parent = parent;
        this.userObject = userObject;
    }

    public Object getUserObject()
    {
        return this.userObject;
    }

    public TreeNode getChildAt(int childIndex)
    {
        return null;
    }

    public int getChildCount()
    {
        return 0;
    }

    public TreeNode getParent()
    {
        return this.parent;
    }

    public int getIndex(TreeNode node)
    {
        return -1;
    }

    public boolean getAllowsChildren()
    {
        return false;
    }

    public boolean isLeaf()
    {
        return true;
    }

    public Enumeration children()
    {
        return null;
    }
}

