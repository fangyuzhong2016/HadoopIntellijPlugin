package com.fangyuzhong.intelliJ.hadoop.fsconnection.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class RefreshObjectsStatusAction
        extends DumbAwareAction
{
    private FileSystemObjectBundle fileSystemObjectBundle;

    private ConnectionHandler connectionHandler;

    public RefreshObjectsStatusAction(FileSystemObjectBundle fileSystemObjectBundle)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.REFRESHOBJECTSSTATUSACTION), "", Icons.ACTION_REFRESH);
        this.fileSystemObjectBundle = fileSystemObjectBundle;
        connectionHandler = fileSystemObjectBundle.getConnectionHandler();
        getTemplatePresentation().setEnabled(connectionHandler.isConnected());
    }

    public void actionPerformed(AnActionEvent anActionEvent)
    {
        if(ActionUtil.TestHdfsConnect(anActionEvent.getProject(),connectionHandler)) {
            fileSystemObjectBundle.refreshTreeChildren();
        }
    }
}

