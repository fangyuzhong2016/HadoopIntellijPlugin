package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.ToolTipProvider;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectType;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;

import javax.swing.*;
import javax.swing.tree.TreeNode;
import java.util.List;

/**
 * 定义HDFS目录树节点接口<p>
 * Created by fangyuzhong on 17-7-15.
 */
 public interface FileSystemBrowserTreeNode
        extends TreeNode, NavigationItem, ItemPresentation, ToolTipProvider,GenericFileSystemElement
{
    /**
     * 初始化树的元素
     */
     void initTreeElement();

    /**
     * 是否展开
     * @return
     */
     boolean canExpand();

    /**
     * 获取树的深度
     * @return
     */
     int getTreeDepth();

    /**
     * 树结构是否加载
     * @return
     */
     boolean isTreeStructureLoaded();

    /**
     * 或节点的子节点
     * @return
     */
     List<? extends FileSystemBrowserTreeNode> getChildren();

    /**
     * 刷新子节点
     * @param
     */
     void refreshTreeChildren();

    /**
     * 重构该节点的子节点
     */
     void rebuildTreeChildren();

    /**
     * 获取节点图标
     * @param paramInt
     * @return
     */
     Icon getIcon(int paramInt);

    /**
     *获取节点文本
     * @return
     */
     String getPresentableText();

    /**
     * 获取节点文本详细信息
     * @return
     */
     String getPresentableTextDetails();

    /**
     *
     * @return
     */
     String getPresentableTextConditionalDetails();

    /**
     * 获取子节点
     * @param paramInt
     * @return
     */
     FileSystemBrowserTreeNode getChildAt(int paramInt);

    /**
     * 获取父节点
     * @return
     */
     FileSystemBrowserTreeNode getParent();

    /**
     * 获取节点所在的索引
     * @param paramBrowserTreeNode
     * @return
     */
     int getIndex(FileSystemBrowserTreeNode paramBrowserTreeNode);
}
