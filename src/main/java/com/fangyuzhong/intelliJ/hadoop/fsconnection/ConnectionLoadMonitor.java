package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.Counter;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.intellij.openapi.Disposable;

/**
 * 定义连接加载监控对象
 * Created by fangyuzhong on 17-7-15.
 */
public class ConnectionLoadMonitor
        implements Disposable
{
    private ConnectionHandlerRef connectionHandlerRef;

    public ConnectionLoadMonitor(ConnectionHandler connectionHandler)
    {
        this.connectionHandlerRef = connectionHandler.getRef();
    }

    public ConnectionHandler getConnectionHandler()
    {
        return this.connectionHandlerRef.get();
    }

    private Counter runningStatements = new Counter()
    {
        public void onIncrement()
        {
            ConnectionLoadMonitor.this.updateLastAccess();
        }

        public void onDecrement()
        {
            ConnectionLoadMonitor.this.updateLastAccess();
        }
    };
    private Counter runningMethods = new Counter()
    {
        public void onIncrement()
        {
            ConnectionLoadMonitor.this.updateLastAccess();
        }

        public void onDecrement()
        {
            ConnectionLoadMonitor.this.updateLastAccess();
        }
    };
    private Counter runningMetaLoaders = new Counter()
    {
        public void onIncrement()
        {
            ConnectionLoadMonitor.this.updateLastAccess();
        }

        public void onDecrement()
        {
            ConnectionLoadMonitor.this.updateLastAccess();
            ConnectionHandler connectionHandler = ConnectionLoadMonitor.this.getConnectionHandler();
            if ((getValue() == 0) && (!connectionHandler.isDisposed()) && (!connectionHandler.isVirtual()))
            {
                ((ConnectionLoadListener) EventUtil.notify(connectionHandler.getProject(), ConnectionLoadListener.TOPIC)).contentsLoaded(connectionHandler);
            }
        }
    };

    private void updateLastAccess()
    {
        ConnectionHandler connectionHandler = getConnectionHandler();
    }

    public Counter getRunningMetaLoaders()
    {
        return this.runningMetaLoaders;
    }

    public Counter getRunningStatements()
    {
        return this.runningStatements;
    }

    public Counter getRunningMethods()
    {
        return this.runningMethods;
    }

    public boolean isLoading()
    {
        return this.runningMetaLoaders.getValue() > 0;
    }

    public void dispose()
    {
    }

    public boolean isIdle()
    {
        return (this.runningStatements.getValue() == 0) && (this.runningMethods.getValue() == 0);
    }
}
