package com.fangyuzhong.intelliJ.hadoop.core.ui.tree;

/**
 * 定义树事件类型
 * Created by fangyuzhong on 17-7-14.
 */
public enum TreeEventType
{
    /**
     *树节点加载完成
     */
    NODES_ADDED,
    /**
     * 树节点移除完成
     */
    NODES_REMOVED,
    /**
     * 树节点改变
     */
    NODES_CHANGED,
    /**
     *树结构改变
     */
    STRUCTURE_CHANGED;

    private TreeEventType()
    {
    }
}
