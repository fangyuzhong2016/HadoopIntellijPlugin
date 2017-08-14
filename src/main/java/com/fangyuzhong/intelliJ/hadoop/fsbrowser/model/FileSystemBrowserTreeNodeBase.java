package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreeNode;
import java.util.Collections;
import java.util.Enumeration;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public abstract class FileSystemBrowserTreeNodeBase extends DisposableBase
        implements FileSystemBrowserTreeNode
{
    /**
     * 获取当前位置字符串
     * @return
     */
    @Nullable
    public String getLocationString()
    {
        return null;
    }

    /**
     * 获取子对象枚举
     * @return
     */
    public Enumeration children()
    {
        return Collections.enumeration(getChildren());
    }

    /**
     * 获取子节点所在的索引
     * @param child
     * @return
     */
    public int getIndex(TreeNode child)
    {
        return getIndex((FileSystemBrowserTreeNode) child);
    }


    public boolean getAllowsChildren()
    {
        return !isLeaf();
    }
}
