package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.intellij.openapi.Disposable;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public interface ContentDependencyAdapter
        extends Disposable
{
     boolean canConnect(ConnectionHandler paramConnectionHandler);

     boolean canLoad(ConnectionHandler paramConnectionHandler);

     void markSourcesDirty();

     boolean isDirty();

     void beforeLoad();

     void afterLoad();

     void beforeReload(DynamicContent paramDynamicContent);

     void afterReload(DynamicContent paramDynamicContent);

     boolean isSubContent();

     boolean canLoadFast();
}