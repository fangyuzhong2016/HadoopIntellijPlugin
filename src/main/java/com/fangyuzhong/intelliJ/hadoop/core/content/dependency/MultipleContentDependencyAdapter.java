package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposerUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class MultipleContentDependencyAdapter
        extends BasicDependencyAdapter
        implements ContentDependencyAdapter
{
    private List<ContentDependency> dependencies;

    public MultipleContentDependencyAdapter(DynamicContent... sourceContents)
    {
        for (DynamicContent sourceContent : sourceContents)
        {
            if (sourceContent != null)
            {
                if (this.dependencies == null)
                {
                    this.dependencies = new ArrayList();
                }
                this.dependencies.add(new BasicContentDependency(sourceContent));
            }
        }
    }

    public boolean canLoad(ConnectionHandler connectionHandler)
    {
        if ((this.dependencies != null) && (canConnect(connectionHandler)))
        {
            for (ContentDependency dependency : this.dependencies)
            {
                if (!dependency.getSourceContent().isLoaded())
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public void markSourcesDirty()
    {
        if (this.dependencies != null)
        {
            for (ContentDependency dependency : this.dependencies)
            {
                dependency.markSourcesDirty();
            }
        }
    }

    public boolean isDirty()
    {
        if (this.dependencies != null)
        {
            for (ContentDependency dependency : this.dependencies)
            {
                if (dependency.isDirty())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canLoadFast()
    {
        if (this.dependencies != null)
        {
            for (ContentDependency dependency : this.dependencies)
            {
                if (!dependency.getSourceContent().isLoaded())
                {
                    return false;
                }
            }
        }
        return true;
    }

    public void beforeLoad()
    {
        if (this.dependencies != null)
        {
            for (ContentDependency dependency : this.dependencies)
            {
                dependency.getSourceContent().load(false);
            }
        }
    }

    public void afterLoad()
    {
        if (this.dependencies != null)
        {
            for (ContentDependency dependency : this.dependencies)
            {
                dependency.reset();
            }
        }
    }

    public void beforeReload(DynamicContent dynamicContent)
    {
        beforeLoad();
    }

    public void afterReload(DynamicContent dynamicContent)
    {
        afterLoad();
    }

    public void dispose()
    {
        DisposerUtil.dispose(this.dependencies);
        this.dependencies = null;
        super.dispose();
    }
}

