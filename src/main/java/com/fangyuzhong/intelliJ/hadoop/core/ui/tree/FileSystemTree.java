package com.fangyuzhong.intelliJ.hadoop.core.ui.tree;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.fangyuzhong.intelliJ.hadoop.core.ui.GUIUtil;
import com.intellij.ui.treeStructure.Tree;
import com.intellij.util.ui.UIUtil;

import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * 定义文件系统树控件，继承IntelliJ 的Tree
 * Created by fangyuzhong on 17-7-14.
 */
public class FileSystemTree extends Tree implements Disposable
{
    public static final DefaultTreeCellRenderer DEFAULT_CELL_RENDERER = new DefaultTreeCellRenderer();
    private boolean disposed;

    public FileSystemTree()
    {
        setTransferHandler(new FileSystemTreeTransferHandler());
    }

    public FileSystemTree(TreeModel treemodel)
    {
        super(treemodel);
        setTransferHandler(new FileSystemTreeTransferHandler());
        setFont(UIUtil.getLabelFont());
    }

    public FileSystemTree(TreeNode root)
    {
        super(root);
        setTransferHandler(new FileSystemTreeTransferHandler());
    }

    public void dispose()
    {
        if (!this.disposed)
        {
            this.disposed = true;
            setModel(null);
            setSelectionModel(null);
            GUIUtil.removeListeners(this);
            getUI().uninstallUI(this);
        }
    }

    public boolean isDisposed()
    {
        return this.disposed;
    }
}
