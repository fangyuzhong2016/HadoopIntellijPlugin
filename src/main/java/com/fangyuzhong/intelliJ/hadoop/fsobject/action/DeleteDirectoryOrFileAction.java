package com.fangyuzhong.intelliJ.hadoop.fsobject.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.hadoop.HDFSUtil;
import com.fangyuzhong.intelliJ.hadoop.core.message.MessageCallback;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectType;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class DeleteDirectoryOrFileAction extends AnAction
{
    ConnectionHandler connectionHandler;
    FileSystemObject fileSystemObject;
    public DeleteDirectoryOrFileAction(ConnectionHandler connectionHandler, FileSystemObject fileSystemObject)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DELETEDIRECTORYORFILEACTION),"", Icons.ACTION_DELETE);
        this.connectionHandler=connectionHandler;
        this.fileSystemObject=fileSystemObject;
        FsAction fsAction = HDFSUtil.getDirFileActionByUser(connectionHandler.getMainFileSystem(),
                this.fileSystemObject.getLocationString(),connectionHandler.getSettings().getFileSystemSettings().getUser());
        if(fsAction==FsAction.ALL||fsAction==FsAction.WRITE||fsAction==FsAction.WRITE_EXECUTE||fsAction==FsAction.READ_WRITE)
        {
            getTemplatePresentation().setEnabled(true);
        }
        else
        {
            getTemplatePresentation().setEnabled(false);
        }
    }
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        if (ActionUtil.TestHdfsConnect(e.getProject(),connectionHandler))
        {
            String strPath = fileSystemObject.getLocationString();
            FileSystemObject parentObj = fileSystemObject.getParentObject();
            ResourceBundle resourceBundle = LocaleLanguageManager.getInstance().getResourceBundle();

            String message = resourceBundle.getString(LanguageKeyWord.DELETEDIRECTORYTITLE);
            if (fileSystemObject.getObjectType() == FileSystemObjectType.FILE)
            {
                message =  resourceBundle.getString(LanguageKeyWord.DELETEFILETITLE);;
            }
            MessageUtil.showQuestionDialog(e.getProject(), resourceBundle.getString(LanguageKeyWord.MESSAGETILE),
                    message, new String[]{resourceBundle.getString(LanguageKeyWord.ASKYES),
                            resourceBundle.getString(LanguageKeyWord.ASKNO)}, 0, new MessageCallback(Integer.valueOf(0))
                    {
                        protected void execute()
                        {
                            boolean isSuccess = false;
                            String error = "";
                            try
                            {
                                isSuccess = connectionHandler.getMainFileSystem().delete(new Path(strPath), true);
                            } catch (Exception ex)
                            {
                                error = ex.toString();
                                isSuccess = false;
                            }
                            if (!isSuccess)
                            {
                                MessageUtil.showInfoDialog(e.getProject(),
                                        LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                                        LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DELETEFAILED) + error);
                            }
                            if (parentObj == null)
                            {
                                connectionHandler.getObjectBundle().refreshTreeChildren();
                            } else
                            {
                                parentObj.rebuildTreeChildren();
                            }
                            fileSystemObject.dispose();
                        }
                    });
        }
    }

}
