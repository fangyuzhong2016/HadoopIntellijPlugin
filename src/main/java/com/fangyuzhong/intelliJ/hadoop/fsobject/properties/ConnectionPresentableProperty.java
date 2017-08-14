package com.fangyuzhong.intelliJ.hadoop.fsobject.properties;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandlerRef;
import com.intellij.pom.Navigatable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class ConnectionPresentableProperty
        extends PresentableProperty
{
    private ConnectionHandlerRef connectionHandlerRef;

    public ConnectionPresentableProperty(ConnectionHandler connectionHandler)
    {
        this.connectionHandlerRef = connectionHandler.getRef();
    }

    public ConnectionHandler getConnectionHandler()
    {
        return this.connectionHandlerRef.get();
    }

    public String getName()
    {
        return "Connection";
    }

    public String getValue()
    {
        return getConnectionHandler().getName();
    }

    public Icon getIcon()
    {
        return getConnectionHandler().getIcon();
    }

    public Navigatable getNavigatable()
    {
        return getConnectionHandler().getObjectBundle();
    }
}