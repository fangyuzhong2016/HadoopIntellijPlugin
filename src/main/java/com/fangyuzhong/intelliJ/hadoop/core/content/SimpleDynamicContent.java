package com.fangyuzhong.intelliJ.hadoop.core.content;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.BasicDependencyAdapter;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.ContentDependencyAdapter;
import com.fangyuzhong.intelliJ.hadoop.core.content.loader.DynamicContentLoader;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class SimpleDynamicContent<T extends DynamicContentElement>
        extends DynamicContentImpl<T>
{
    private static ContentDependencyAdapter DEPENDENCY_ADAPTER = new BasicDependencyAdapter();

    public SimpleDynamicContent(@NotNull GenericFileSystemElement parent, DynamicContentLoader<T> loader, boolean indexed)
    {
        super(parent, loader, DEPENDENCY_ADAPTER, indexed);
    }

    @Nullable
    public Filter<T> getFilter()
    {
        return null;
    }

    public void notifyChangeListeners()
    {
    }

    public Project getProject()
    {
        return null;
    }

    public String getContentDescription()
    {
        return null;
    }

    public String getName()
    {
        return null;
    }
}