package com.fangyuzhong.intelliJ.hadoop.fsconnection.action;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsobject.action.CreateDirectoryAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class ConnectionActionGroup
        extends DefaultActionGroup
{
    public ConnectionActionGroup(ConnectionHandler connectionHandler)
    {
        add(new ConnectAction(connectionHandler));
        add(new DisconnectAction(connectionHandler));
        addSeparator();
        add(new RefreshObjectsStatusAction(connectionHandler.getObjectBundle()));
        addSeparator();
        add(new CreateDirectoryAction(connectionHandler,connectionHandler.getObjectBundle()));
    }
}
