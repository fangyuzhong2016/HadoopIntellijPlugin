package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.intellij.openapi.project.Project;

/**
 * 定义简单的TreeModel的实现
 * Created by fangyuzhong on 17-7-16.
 */

public class SimpleBrowserTreeModel
        extends BrowserTreeModel
{
    /**
     * 初始化
     */
    public SimpleBrowserTreeModel()
    {
        this(FailsafeUtil.DUMMY_PROJECT, null);
    }

    /**
     * 初始化
     * @param project
     * @param connectionBundle
     */
    public SimpleBrowserTreeModel(Project project, ConnectionBundle connectionBundle)
    {
        super(new SimpleBrowserTreeRoot(project, connectionBundle));
    }

    /**
     * 判断是否包含指定的TreeNode
     * @param node
     * @return
     */
    public boolean contains(FileSystemBrowserTreeNode node)
    {
        return true;
    }

    /**
     *
     */
    public void dispose()
    {
        super.dispose();
    }
}