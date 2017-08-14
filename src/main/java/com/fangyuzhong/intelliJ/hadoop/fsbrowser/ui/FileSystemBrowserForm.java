package com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableProjectComponent;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemBaseFormImpl;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import org.jetbrains.annotations.Nullable;

/**
 * 定义文件系统Tree窗体UI抽象类
 * Created by fangyuzhong on 17-7-16.
 */
public abstract class FileSystemBrowserForm
        extends FileSystemBaseFormImpl<DisposableProjectComponent>
{
    /**
     * 初始化
     * @param parentComponent
     */
    protected FileSystemBrowserForm(DisposableProjectComponent parentComponent)
    {
        super(parentComponent);
    }

    /**
     * 获取Tree控件
     * @return
     */
    @Nullable
    public abstract FileSystemBrowserTree getBrowserTree();

    /**
     * 设置选择要素
     * @param paramBrowserTreeNode
     * @param paramBoolean1
     * @param paramBoolean2
     */
    public abstract void selectElement(FileSystemBrowserTreeNode paramBrowserTreeNode, boolean paramBoolean1, boolean paramBoolean2);

    /**
     * 构建文件系统Tree
     */
    public abstract void rebuildTree();

    /**
     * 资源释放
     */
    public void dispose()
    {
        super.dispose();
    }
}
