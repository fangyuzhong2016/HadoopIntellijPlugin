package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.core.ui.tree.TreeEventType;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public abstract class BrowserTreeEventAdapter
        implements BrowserTreeEventListener
{
    public void nodeChanged(FileSystemBrowserTreeNode node, TreeEventType eventType)
    {
    }

    public void selectionChanged()
    {
    }
}
