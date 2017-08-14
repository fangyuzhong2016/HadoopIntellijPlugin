package com.fangyuzhong.intelliJ.hadoop.fsconnection.action;

import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.io.IOException;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class ConnectAction
        extends AbstractConnectionAction
{
    public ConnectAction(ConnectionHandler connectionHandler)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.CONNECTACTION), "连接到 " + connectionHandler.getName(), null, connectionHandler);
        getTemplatePresentation().setEnabled(!connectionHandler.isConnected());
    }

    public void actionPerformed(AnActionEvent anActionEvent)
    {
        final ConnectionHandler connectionHandler = getConnectionHandler();
        //TODO 添加连接到文件系统action的实现
        if(!ActionUtil.TestHdfsConnect(anActionEvent.getProject(),connectionHandler))
        {
            return;
        }
        connectionHandler.getConnectionStatus().setConnected(true);
        connectionHandler.getObjectBundle().refreshTreeChildren();
    }
}
