package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.ProjectRef;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectType;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.FileStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义文件树的根节点
 * Created by fangyuzhong on 17-7-16.
 */
public class SimpleBrowserTreeRoot
        extends FileSystemBrowserTreeNodeBase

{
    private List<ConnectionBundle> rootChildren;
    private ProjectRef projectRef;

    /**
     *
     * @param project
     * @param connectionBundle
     */
    public SimpleBrowserTreeRoot(@NotNull Project project, ConnectionBundle connectionBundle)
    {
        this.projectRef = new ProjectRef(project);
        this.rootChildren = new ArrayList();
        if (connectionBundle != null)
        {
            this.rootChildren.add(connectionBundle);
        }
    }

    /**
     *
     * @return
     */
    @NotNull
    public Project getProject()
    {
        return FailsafeUtil.get(this.projectRef.get());
    }

    @Nullable
    public GenericFileSystemElement getParentElement()
    {
        return null;
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
        return true;
    }

    public int getTreeDepth()
    {
        return 0;
    }

    @Nullable
    public FileSystemBrowserTreeNode getParent()
    {
        return null;
    }

    public List<ConnectionBundle> getChildren()
    {
        return (List) FailsafeUtil.get(this.rootChildren);
    }

    public void refreshTreeChildren()
    {
        for (ConnectionBundle connectionBundle : getChildren())
        {
            connectionBundle.refreshTreeChildren();
        }
    }

    public void rebuildTreeChildren()
    {
        for (ConnectionBundle connectionBundle : getChildren())
        {
            connectionBundle.rebuildTreeChildren();
        }
    }

    public FileSystemBrowserTreeNode getChildAt(int index)
    {
        return getChildren().get(index);
    }

    public int getChildCount()
    {
        return getChildren().size();
    }

    public boolean isLeaf()
    {
        return false;
    }

    public int getIndex(FileSystemBrowserTreeNode child)
    {
        return getChildren().indexOf(child);
    }

    public Icon getIcon(int flags)
    {
        return Icons.FILE_SYSTEM_HDFS;
    }

    public String getPresentableText()
    {
        return "Connection Managers";
    }

    public String getPresentableTextDetails()
    {
        return null;
    }

    public String getPresentableTextConditionalDetails()
    {
        return null;
    }

    @Nullable
    public ConnectionHandler getConnectionHandler()
    {
        return null;
    }

    public GenericFileSystemElement getUndisposedElement()
    {
        return this;
    }

    @Nullable
    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType)
    {
        return null;
    }

    public String getToolTip()
    {
        return null;
    }

    public String getName()
    {
        return getPresentableText();
    }

    public void navigate(boolean b)
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

    public ItemPresentation getPresentation()
    {
        return this;
    }

    public FileStatus getFileStatus()
    {
        return FileStatus.NOT_CHANGED;
    }

    public String getLocationString()
    {
        return null;
    }

    public Icon getIcon(boolean open)
    {
        return getIcon(0);
    }


    public void dispose()
    {
        super.dispose();
        if (this.rootChildren != null)
        {
            this.rootChildren.clear();
            this.rootChildren = null;
        }
    }
}
