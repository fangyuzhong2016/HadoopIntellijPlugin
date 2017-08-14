package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.VirtualConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNodeBase;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class FileSystemVirtualObjectBundle
        extends FileSystemBrowserTreeNodeBase
        implements FileSystemObjectBundle
{
    private VirtualConnectionHandler connectionHandler;

    public FileSystemVirtualObjectBundle(VirtualConnectionHandler connectionHandler)
    {
        this.connectionHandler = connectionHandler;
    }

    @Nullable
    public FileSystemObject getObject(FileSystemObjectType objectType, String name)
    {
        return null;
    }

    @Nullable
    public FileSystemObject getObject(FileSystemObjectType objectType, String name, int overload)
    {
        return null;
    }


    public boolean isValid()
    {
        return false;
    }

    @NotNull
    public ConnectionHandler getConnectionHandler()
    {
        return this.connectionHandler;
    }

    @NotNull
    public Project getProject()
    {
       return null;
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
        return 0;
    }

    public boolean isTreeStructureLoaded()
    {
        return false;
    }

    public FileSystemBrowserTreeNode getChildAt(int index)
    {
        return null;
    }

    @Nullable
    public FileSystemBrowserTreeNode getParent()
    {
        return null;
    }

    public List<? extends FileSystemBrowserTreeNode> getChildren()
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
        return false;
    }

    public int getIndex(FileSystemBrowserTreeNode child)
    {
        return 0;
    }

    public Icon getIcon(int flags)
    {
        return null;
    }

    public String getPresentableText()
    {
        return null;
    }

    @Nullable
    public String getLocationString()
    {
        return null;
    }

    @Nullable
    public Icon getIcon(boolean unused)
    {
        return null;
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
    public GenericFileSystemElement getParentElement()
    {
        return null;
    }

    public GenericFileSystemElement getUndisposedElement()
    {
        return null;
    }

    @Nullable
    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType)
    {
        return null;
    }

    @Nullable
    public String getName()
    {
        return null;
    }

    @Nullable
    public ItemPresentation getPresentation()
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

    public String getToolTip()
    {
        return null;
    }
}
