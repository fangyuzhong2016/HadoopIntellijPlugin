package com.fangyuzhong.intelliJ.hadoop.core.ui.tree;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class FolderTreeNode extends LeafTreeNode
{
    List<TreeNode> children = new ArrayList();

    public FolderTreeNode(TreeNode parent, Object userObject, List<TreeNode> children)
    {
        super(parent, userObject);
        this.children = children;
    }

    public void addChild(TreeNode child)
    {
        this.children.add(child);
    }

    public TreeNode getChildAt(int childIndex)
    {
        return (TreeNode) this.children.get(childIndex);
    }

    public int getChildCount()
    {
        return this.children.size();
    }

    public int getIndex(TreeNode node)
    {
        return this.children.indexOf(node);
    }

    public boolean getAllowsChildren()
    {
        return true;
    }

    public boolean isLeaf()
    {
        return false;
    }

    public Enumeration children()
    {
        final Iterator iterator = this.children.iterator();
       return new Enumeration()
        {
            public boolean hasMoreElements()
            {
                return iterator.hasNext();
            }

            public Object nextElement()
            {
                return iterator.next();
            }
        };
    }
}
