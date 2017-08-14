package com.fangyuzhong.intelliJ.hadoop.fsconnection.action;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandlerRef;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public abstract class AbstractConnectionAction
        extends DumbAwareAction
{
    private ConnectionHandlerRef connectionHandlerRef;

    public AbstractConnectionAction(String text, @NotNull ConnectionHandler connectionHandler)
    {
        this(text, null, connectionHandler);
    }

    public AbstractConnectionAction(String text, Icon icon, @NotNull ConnectionHandler connectionHandler)
    {
        this(text, null, icon, connectionHandler);
    }

    public AbstractConnectionAction(String text, String description, Icon icon, @NotNull ConnectionHandler connectionHandler)
    {
        super(text, description, icon);
        this.connectionHandlerRef = connectionHandler.getRef();
    }

    @NotNull
    public ConnectionHandler getConnectionHandler()
    {
        return   this.connectionHandlerRef.get();
    }
}