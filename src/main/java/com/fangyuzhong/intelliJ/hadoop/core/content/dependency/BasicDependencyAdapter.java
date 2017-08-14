package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class BasicDependencyAdapter
        implements ContentDependencyAdapter
{
    public boolean canConnect(ConnectionHandler connectionHandler)
    {
        return (connectionHandler != null) && (connectionHandler.canConnect()) && (connectionHandler.isValid());
    }

    public boolean canLoad(ConnectionHandler connectionHandler)
    {
        return canConnect(connectionHandler);
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
        return false;
    }

    public boolean isSubContent()
    {
        return false;
    }

    public void dispose()
    {
    }
}
