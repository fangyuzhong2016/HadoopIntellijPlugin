package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.core.LoadIcon;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectType;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatus;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class LoadInProgressTreeNode extends FileSystemBrowserTreeNodeBase
{
    private FileSystemBrowserTreeNode parent;
    private List list;

    public LoadInProgressTreeNode( FileSystemBrowserTreeNode parent)
    {
        this.parent = parent;
    }

    public List asList()
    {
        if (this.list == null)
        {
            synchronized (this)
            {
                if (this.list == null)
                {
                    this.list = new List();
                    this.list.add(this);
                }
            }
        }
        return this.list;
    }

    public boolean isTreeStructureLoaded()
    {
        return true;
    }

    public void initTreeElement()
    {
    }

    public boolean canExpand()
    {
        return false;
    }

    public int getTreeDepth()
    {
        return getParent().getTreeDepth() + 1;
    }

    public FileSystemBrowserTreeNode getChildAt(int index)
    {
        return null;
    }

    public FileSystemBrowserTreeNode getParent()
    {
        return  FailsafeUtil.get(this.parent);
    }

    public List getChildren()
    {
        return null;
    }

    public void refreshTreeChildren()
    {
    }

    public void rebuildTreeChildren()
    {
    }

    public int getChildCount()
    {
        return 0;
    }

    public boolean isLeaf()
    {
        return true;
    }

    public int getIndex(FileSystemBrowserTreeNode child)
    {
        return -1;
    }

    public Icon getIcon(int flags)
    {
        return LoadIcon.INSTANCE;
    }

    public String getPresentableText()
    {
        return "Loading...";
    }

    public String getPresentableTextDetails()
    {
        return null;
    }

    public String getPresentableTextConditionalDetails()
    {
        return null;
    }

    @NotNull
    public ConnectionHandler getConnectionHandler()
    {
       return FailsafeUtil.get(getParent().getConnectionHandler());
    }

    public Project getProject()
    {
        return getParent().getProject();
    }

    public GenericFileSystemElement getParentElement()
    {
        return null;
    }

    public GenericFileSystemElement getUndisposedElement()
    {
        return this;
    }

    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType)
    {
        return null;
    }

    public String getLocationString()
    {
        return null;
    }

    public Icon getIcon(boolean open)
    {
        return null;
    }


    public void navigate(boolean requestFocus)
    {
    }

    public boolean canNavigate()
    {
        return false;
    }

    public boolean canNavigateToSource()
    {
        return false;
    }

    public String getName()
    {
        return null;
    }

    public ItemPresentation getPresentation()
    {
        return this;
    }

    public FileStatus getFileStatus()
    {
        return FileStatus.NOT_CHANGED;
    }

    public String getToolTip()
    {
        return null;
    }

    public void dispose()
    {
        super.dispose();
        this.parent = null;
    }

    public class List
            extends ArrayList<FileSystemBrowserTreeNode>
            implements Disposable
    {
        public List()
        {
        }

        public void dispose()
        {
            if (size() > 0)
            {
                FileSystemBrowserTreeNode browserTreeNode = get(0);
                browserTreeNode.dispose();
                clear();
            }
        }
    }
}
