package com.fangyuzhong.intelliJ.hadoop.fsobject.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class RefreshDirectoryAction
        extends AnAction
{

    FileSystemObject fileSystemObject;

    public RefreshDirectoryAction(FileSystemObject fileSystemObject)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.REFRESHDIRECTORYACTION), "", Icons.ACTION_REFRESH);
        this.fileSystemObject =  fileSystemObject;

    }
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        if(ActionUtil.TestHdfsConnect(e.getProject(),fileSystemObject.getConnectionHandler()))
        {
            fileSystemObject.rebuildTreeChildren();
        }
    }
}
