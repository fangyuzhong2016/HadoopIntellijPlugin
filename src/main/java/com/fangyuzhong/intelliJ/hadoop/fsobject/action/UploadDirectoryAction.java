package com.fangyuzhong.intelliJ.hadoop.fsobject.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.hadoop.HDFSUtil;
import com.fangyuzhong.intelliJ.hadoop.core.thread.BackgroundTask;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.hadoop.fs.permission.FsAction;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class UploadDirectoryAction
        extends AnAction
{
    ConnectionHandler connectionHandler=null;
    FileSystemObject fileSystemObject=null;
    String strLocalPath="";
    public UploadDirectoryAction(ConnectionHandler connectionHandler,FileSystemObject fileSystem)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.UPLOADDIRECTORYACTION),"", Icons.ACTION_UPLOAD);
        this.connectionHandler=connectionHandler;
        this.fileSystemObject=fileSystem;
        FsAction fsAction = HDFSUtil.getDirFileActionByUser(connectionHandler.getMainFileSystem(),
                this.fileSystemObject.getLocationString(),connectionHandler.getSettings().getFileSystemSettings().getUser());
        if(fsAction==FsAction.ALL||fsAction==FsAction.WRITE||fsAction==FsAction.WRITE_EXECUTE)
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
        if(!ActionUtil.TestHdfsConnect(e.getProject(),connectionHandler))
        {
            return ;
        }
        FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true,
                false, false, false, false);
        VirtualFile[] virtualFiles = FileChooser.chooseFiles(fileChooserDescriptor, e.getProject(), null);
        if (virtualFiles.length == 1)
        {
            strLocalPath = virtualFiles[0].getPath();
        }
        File fileTmp = new File(strLocalPath);

        if (!fileTmp.exists())
        {
            MessageUtil.showErrorDialog(e.getProject(),
                    LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                    LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.UPDIRPATHERROR));
            return;
        }
        String DirName = fileTmp.getName();
        String strhdfspath = fileSystemObject.getLocationString();
       if(HDFSUtil.dirExists(strhdfspath+"/"+DirName,connectionHandler.getMainFileSystem()))
       {
           MessageUtil.showErrorDialog(e.getProject(),
                   LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                   LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.UPNAMEERROR));
           return;
       }
       String taskTitle =  LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.UPTASKTEXT);
        new BackgroundTask(e.getProject(), taskTitle, true)
        {
            public void execute(@NotNull ProgressIndicator progressIndicator)
            {
                boolean isSuccess = HDFSUtil.copyFromLocalFile(strLocalPath, strhdfspath, connectionHandler.getMainConnection(),
                        true, connectionHandler.getMainFileSystem(), progressIndicator);
                if (!isSuccess)
                {
                    MessageUtil.showErrorDialog(e.getProject(), LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                            LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.UPFAILED));
                } else
                {
                    MessageUtil.showInfoDialog(e.getProject(), LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                            LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.UPSUCCESS));
                }
            }
        }.start();
    }
}