package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;

/**
 *文件系统树以Tab列表的方式展示时，树的Node操作使用的是 TabbedBrowserTreeModel
 * Created by fangyuzhong on 17-7-16.
 */
public class TabbedBrowserTreeModel
        extends BrowserTreeModel
{
    /**
     * 初始化TreeModel
     * @param connectionHandler 文件系统连接处理接口
     */
    public TabbedBrowserTreeModel(ConnectionHandler connectionHandler)
    {
        super(connectionHandler.getObjectBundle());
    }

    /**
     * 判断是否包含指定的节点对象
     * @param node 指定的TreeNode
     * @return
     */
    public boolean contains(FileSystemBrowserTreeNode node)
    {
        return getConnectionHandler() == node.getConnectionHandler();
    }

    /**
     * 获取文件连接处理对象
     * @return
     */
    public ConnectionHandler getConnectionHandler()
    {
        return getRoot().getConnectionHandler();
    }
}