package com.fangyuzhong.intelliJ.hadoop.fsbrowser;

import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class TreeNavigationHistory
        implements Disposable
{
    private List<FileSystemBrowserTreeNode> history = new ArrayList();
    private int offset;

    public void add(FileSystemBrowserTreeNode treeNode)
    {
        if ((this.history.size() > 0) && (treeNode == this.history.get(this.offset)))
        {
            return;
        }
        while (this.history.size() > this.offset + 1)
        {
            this.history.remove(this.offset + 1);
        }
       // DatabaseBrowserSettings browserSettings = DatabaseBrowserSettings.getInstance(treeNode.getProject());

       // int historySize = ((Integer) browserSettings.getGeneralSettings().getNavigationHistorySize().value()).intValue();
       int historySize=500;//定义存储历史记录的大小 500次
        while (this.history.size() > historySize)
        {
            this.history.remove(0);
        }
        this.history.add(treeNode);
        this.offset = (this.history.size() - 1);
    }

    public void clear()
    {
        this.history.clear();
    }

    public boolean hasNext()
    {
        return this.offset < this.history.size() - 1;
    }

    public boolean hasPrevious()
    {
        return this.offset > 0;
    }

    @Nullable
    public FileSystemBrowserTreeNode next()
    {
        if (this.offset < this.history.size() - 1)
        {
            this.offset += 1;
            FileSystemBrowserTreeNode browserTreeNode =  this.history.get(this.offset);
            if (browserTreeNode.isDisposed())
            {
                this.history.remove(browserTreeNode);
                return next();
            }
            return browserTreeNode;
        }
        return null;
    }

    @Nullable
    public FileSystemBrowserTreeNode previous()
    {
        if (this.offset > 0)
        {
            this.offset -= 1;
            FileSystemBrowserTreeNode browserTreeNode = this.history.get(this.offset);
            if (browserTreeNode.isDisposed())
            {
                this.history.remove(browserTreeNode);
                return previous();
            }
            return browserTreeNode;
        }
        return null;
    }

    public void dispose()
    {
        this.history.clear();
    }
}
