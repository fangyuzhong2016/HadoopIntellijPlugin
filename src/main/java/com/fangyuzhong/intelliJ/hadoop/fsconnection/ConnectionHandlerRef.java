package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class ConnectionHandlerRef
{
    private WeakReference<ConnectionHandler> reference;
    private String connectionId;

    public ConnectionHandlerRef(ConnectionHandler connectionHandler)
    {
        this.reference = new WeakReference(connectionHandler);
        this.connectionId = (connectionHandler == null ? null : connectionHandler.getId());
    }

    public ConnectionHandlerRef(String connectionId)
    {
        this.connectionId = connectionId;
    }

    public String getConnectionId()
    {
        return this.connectionId;
    }

    @NotNull
    public ConnectionHandler get()
    {
        ConnectionHandler connectionHandler = this.reference == null ? null : this.reference.get();
        if (((connectionHandler == null) || (connectionHandler.isDisposed())) && (this.connectionId != null))
        {
            connectionHandler = ConnectionCache.findConnectionHandler(this.connectionId);
            this.reference = new WeakReference(connectionHandler);
        }
        return  FailsafeUtil.get(connectionHandler);

    }

    @Nullable
    public static ConnectionHandlerRef from(ConnectionHandler connectionHandler)
    {
        return connectionHandler == null ? null : connectionHandler.getRef();
    }

    @Nullable
    public static ConnectionHandler get(ConnectionHandlerRef connectionHandlerRef)
    {
        return connectionHandlerRef == null ? null : connectionHandlerRef.get();
    }

    public boolean isValid()
    {
        ConnectionHandler connectionHandler = this.reference == null ? null :  this.reference.get();
        return (connectionHandler != null) && (!connectionHandler.isDisposed());
    }
}
