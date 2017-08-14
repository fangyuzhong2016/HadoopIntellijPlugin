package com.fangyuzhong.intelliJ.hadoop.core.content;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.ContentDependencyAdapter;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.VoidContentDependencyAdapter;
import com.fangyuzhong.intelliJ.hadoop.core.content.loader.DynamicContentLoader;
import com.fangyuzhong.intelliJ.hadoop.core.content.loader.VoidDynamicContentLoader;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class VoidDynamicContent
        implements DynamicContent
{
    private List elements = new ArrayList();
    public static final VoidDynamicContent INSTANCE = new VoidDynamicContent();

    public boolean shouldLoad(boolean force)
    {
        return false;
    }

    public void load(boolean force)
    {
    }

    public void loadInBackground(boolean force)
    {
    }

    public void reload()
    {
    }

    public void refresh()
    {
    }

    public long getChangeTimestamp()
    {
        return 0L;
    }

    public boolean isLoaded()
    {
        return true;
    }

    public boolean isSubContent()
    {
        return false;
    }

    public boolean canLoadFast()
    {
        return true;
    }

    public boolean isLoading()
    {
        return false;
    }

    public boolean isDirty()
    {
        return false;
    }

    public boolean isDisposed()
    {
        return false;
    }

    public void markDirty()
    {
    }

    public Project getProject()
    {
        return null;
    }

    public String getContentDescription()
    {
        return "Empty Content";
    }

    @NotNull
    public List getElements()
    {
        List tmp4_1 = this.elements;
        if (tmp4_1 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/VoidDynamicContent", "getElements"}));
        }
        return tmp4_1;
    }

    public List getElements(String name)
    {
        return null;
    }

    @NotNull
    public List getAllElements()
    {
        List tmp4_1 = getElements();
        if (tmp4_1 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/VoidDynamicContent", "getAllElements"}));
        }
        return tmp4_1;
    }

    @Nullable
    public Filter getFilter()
    {
        return null;
    }

    public DynamicContentElement getElement(String name, int overload)
    {
        return null;
    }

    public void setElements(@Nullable List elements)
    {
    }

    public int size()
    {
        return 0;
    }

    @Nullable
    public GenericFileSystemElement getParentElement()
    {
        return null;
    }

    public DynamicContentLoader getLoader()
    {
        return VoidDynamicContentLoader.INSTANCE;
    }

    public ContentDependencyAdapter getDependencyAdapter()
    {
        return VoidContentDependencyAdapter.INSTANCE;
    }

    public ConnectionHandler getConnectionHandler()
    {
        return null;
    }

    public void updateChangeTimestamp()
    {
    }

    public String getName()
    {
        return "Empty Content";
    }

    public void checkDisposed()
            throws InterruptedException
    {
    }

    public void dispose()
    {
    }
}
