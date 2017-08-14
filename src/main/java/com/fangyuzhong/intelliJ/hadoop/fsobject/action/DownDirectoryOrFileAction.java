package com.fangyuzhong.intelliJ.hadoop.fsobject.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.hadoop.HDFSUtil;
import com.fangyuzhong.intelliJ.hadoop.core.thread.BackgroundTask;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.fsobject.OpenFileListener;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.hadoop.fs.permission.FsAction;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class DownDirectoryOrFileAction
        extends AnAction
{
    ConnectionHandler connectionHandler;
    FileSystemObject fileSystemObject;
    String strLocalPath="";
    public DownDirectoryOrFileAction(ConnectionHandler connectionHandler, FileSystemObject fileSystemObject)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWNDIRECTORYORFILEACTION),"", Icons.ACTION_DOWN);
        this.connectionHandler=connectionHandler;
        this.fileSystemObject = fileSystemObject;
        EventUtil.subscribe(connectionHandler.getProject(), OpenFileListener.TOPIC, this.openFileListener);
        FsAction fsAction = HDFSUtil.getDirFileActionByUser(connectionHandler.getMainFileSystem(),
                this.fileSystemObject.getLocationString(),connectionHandler.getSettings().getFileSystemSettings().getUser());
        if(fsAction==FsAction.ALL||fsAction==FsAction.READ||
                fsAction==FsAction.READ_EXECUTE||fsAction==FsAction.READ_WRITE)
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
        if(ActionUtil.TestHdfsConnect(e.getProject(),connectionHandler))
        {
            //选择下载的保存路径
            FileChooserDescriptor fileChooserDescriptor = new FileChooserDescriptor(false, true,
                    false, false, false, false);
            VirtualFile[] virtualFiles = FileChooser.chooseFiles(fileChooserDescriptor, e.getProject(), null);
            if (virtualFiles.length == 1)
            {
                strLocalPath = virtualFiles[0].getPath();
            }
            if (!new File(strLocalPath).exists())
            {
                MessageUtil.showErrorDialog(e.getProject(),
                        LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                        LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWMSELECTPATHERROR));
                return;
            }
            String strhdfspath = fileSystemObject.getLocationString();
            String strName = fileSystemObject.getName();
            if (new File(strLocalPath + "/" + strName).exists())
            {
                MessageUtil.showErrorDialog(e.getProject(),
                        LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                        LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWNSELECTNAMEERROR));
                return;
            }
            String taskTitle =  LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWNTASKTEXT);
            new BackgroundTask(e.getProject(), taskTitle, true)
            {
                public void execute(@NotNull ProgressIndicator progressIndicator)
                {
                    //HDFSUtil.copyFile(strhdfspath, strLocalPath, connectionHandler.getMainFileSystem(), progressIndicator, e.getProject());
                    boolean isSuccess = HDFSUtil.copyToLocalFile(strhdfspath, strLocalPath, connectionHandler.getMainConnection(),
                            true, connectionHandler.getMainFileSystem(), progressIndicator);
                    if (!isSuccess)
                    {

                        MessageUtil.showErrorDialog(e.getProject(), LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE), LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWNFAILED));
                    } else
                    {
                        MessageUtil.showInfoDialog(e.getProject(), LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE), LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DOWNSUCCESS));
                        //EventUtil.notify(e.getProject(), OpenFileListener.TOPIC).openFile(strLocalPath+"/"+strName,e.getProject());
                    }
                }
            }.start();
        }
    }


    private OpenFileListener openFileListener = new OpenFileListener()
    {
        @Override
        public void openFile(String filePath,Project project)
        {
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
            VirtualFile vf = LocalFileSystem.getInstance().findFileByPath(filePath);
            fileEditorManager.openFile(vf, true, true);
        }
    };
}