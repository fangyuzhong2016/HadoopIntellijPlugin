package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class VoidContentDependencyAdapter
        implements ContentDependencyAdapter
{
    public static final VoidContentDependencyAdapter INSTANCE = new VoidContentDependencyAdapter();

    public boolean canConnect(ConnectionHandler connectionHandler)
    {
        return false;
    }

    public boolean canLoad(ConnectionHandler connectionHandler)
    {
        return false;
    }

    public void markSourcesDirty()
    {
    }

    public boolean isDirty()
    {
        return false;
    }

    public void beforeLoad()
    {
    }

    public void afterLoad()
    {
    }

    public void beforeReload(DynamicContent dynamicContent)
    {
    }

    public void afterReload(DynamicContent dynamicContent)
    {
    }

    public boolean canLoadFast()
    {
        return true;
    }

    public boolean isSubContent()
    {
        return false;
    }

    public void dispose()
    {
    }
}
