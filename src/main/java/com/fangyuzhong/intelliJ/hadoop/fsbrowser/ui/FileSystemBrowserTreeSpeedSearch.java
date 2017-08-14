package com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import com.intellij.openapi.Disposable;
import com.intellij.ui.SpeedSearchBase;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class FileSystemBrowserTreeSpeedSearch
        extends SpeedSearchBase<JTree>
        implements Disposable
{
    private static final Object[] EMPTY_ARRAY = new Object[0];
    private Object[] elements = null;

    public FileSystemBrowserTreeSpeedSearch(FileSystemBrowserTree tree)
    {
        super(tree);
        getComponent().getModel().addTreeModelListener(this.treeModelListener);
    }

    protected int getSelectedIndex()
    {
        Object[] elements = getAllElements();
        FileSystemBrowserTreeNode treeNode = getSelectedTreeElement();
        if (treeNode != null)
        {
            for (int i = 0; i < elements.length; i++)
            {
                if (treeNode == elements[i])
                {
                    return i;
                }
            }
        }
        return -1;
    }

    private FileSystemBrowserTreeNode getSelectedTreeElement()
    {
        TreePath selectionPath = getComponent().getSelectionPath();
        if (selectionPath != null)
        {
            return (FileSystemBrowserTreeNode) selectionPath.getLastPathComponent();
        }
        return null;
    }

    protected Object[] getAllElements()
    {
        if (this.elements == null)
        {
            List<FileSystemBrowserTreeNode> nodes = new ArrayList();
            FileSystemBrowserTreeNode root = getComponent().getModel().getRoot();
            loadElements(nodes, root);
            this.elements = nodes.toArray();
        }
        return this.elements;
    }

    private static void loadElements(List<FileSystemBrowserTreeNode> nodes, FileSystemBrowserTreeNode browserTreeNode)
    {
        if (browserTreeNode.isTreeStructureLoaded())
        {
            if ((browserTreeNode instanceof ConnectionBundle))
            {
                ConnectionBundle connectionBundle = (ConnectionBundle) browserTreeNode;
                for (ConnectionHandler connectionHandler : connectionBundle.getConnectionHandlers())
                {
                    FileSystemObjectBundle objectBundle = connectionHandler.getObjectBundle();
                    loadElements(nodes, objectBundle);
                }
            } else
            {
                for (FileSystemBrowserTreeNode treeNode : browserTreeNode.getChildren())
                {
                    if ((treeNode instanceof FileSystemObject))
                    {
                        nodes.add(treeNode);
                    }
                    loadElements(nodes, treeNode);
                }
            }
        }
    }

    public FileSystemBrowserTree getComponent()
    {
        return (FileSystemBrowserTree) super.getComponent();
    }

    protected String getElementText(Object o)
    {
        FileSystemBrowserTreeNode treeNode = (FileSystemBrowserTreeNode) o;
        return treeNode.getPresentableText();
    }

    protected void selectElement(Object o, String s)
    {
        FileSystemBrowserTreeNode treeNode = (FileSystemBrowserTreeNode) o;
        getComponent().selectElement(treeNode, false);
    }

    TreeModelListener treeModelListener = new TreeModelListener()
    {
        public void treeNodesChanged(TreeModelEvent e)
        {
            FileSystemBrowserTreeSpeedSearch.this.elements = null;
        }

        public void treeNodesInserted(TreeModelEvent e)
        {
            FileSystemBrowserTreeSpeedSearch.this.elements = null;
        }

        public void treeNodesRemoved(TreeModelEvent e)
        {
            FileSystemBrowserTreeSpeedSearch.this.elements = null;
        }

        public void treeStructureChanged(TreeModelEvent e)
        {
            FileSystemBrowserTreeSpeedSearch.this.elements = null;
        }
    };

    public void dispose()
    {
        getComponent().getModel().removeTreeModelListener(this.treeModelListener);
        this.elements = EMPTY_ARRAY;
        this.treeModelListener = null;
    }
}
