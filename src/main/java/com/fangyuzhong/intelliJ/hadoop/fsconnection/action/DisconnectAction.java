package com.fangyuzhong.intelliJ.hadoop.fsconnection.action;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class DisconnectAction
        extends AbstractConnectionAction
{
    public DisconnectAction(ConnectionHandler connectionHandler)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DISCONNECTACTION), "断开【" + connectionHandler.getName()+"】连接", null, connectionHandler);
        getTemplatePresentation().setEnabled(connectionHandler.isConnected());
    }

    public void actionPerformed(AnActionEvent e)
    {
        ConnectionHandler connectionHandler = getConnectionHandler();
        //TODO 添加断开连接的action的实现
        connectionHandler.disconnect();
    }
}
