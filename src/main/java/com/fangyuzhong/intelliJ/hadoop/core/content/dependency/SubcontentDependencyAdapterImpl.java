package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class SubcontentDependencyAdapterImpl
        extends BasicDependencyAdapter
        implements SubcontentDependencyAdapter
{
    private ContentDependency contentDependency;

    public SubcontentDependencyAdapterImpl(@NotNull GenericFileSystemElement sourceContentOwner, @NotNull DynamicContentType sourceContentType)
    {
        this.contentDependency = new LinkedContentDependency(sourceContentOwner, sourceContentType);
    }

    public DynamicContent getSourceContent()
    {
        return this.contentDependency.getSourceContent();
    }

    public boolean canLoad(ConnectionHandler connectionHandler)
    {
        return (canConnect(connectionHandler)) && (this.contentDependency.getSourceContent().isLoaded());
    }

    public void markSourcesDirty()
    {
        this.contentDependency.markSourcesDirty();
    }

    public boolean isDirty()
    {
        return this.contentDependency.isDirty();
    }

    public void beforeLoad()
    {
    }

    public void afterLoad()
    {
        this.contentDependency.reset();
    }

    public void beforeReload(DynamicContent dynamicContent)
    {
    }

    public void afterReload(DynamicContent dynamicContent)
    {
    }

    public boolean canLoadFast()
    {
        return getSourceContent().isLoaded();
    }

    public boolean isSubContent()
    {
        return true;
    }

    public void dispose()
    {
        Disposer.dispose(this.contentDependency);
        this.contentDependency = VoidContentDependency.INSTANCE;
        super.dispose();
    }
}