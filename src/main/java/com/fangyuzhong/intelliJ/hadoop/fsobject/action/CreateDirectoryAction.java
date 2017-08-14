package com.fangyuzhong.intelliJ.hadoop.fsobject.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.hadoop.HDFSUtil;
import com.fangyuzhong.intelliJ.hadoop.core.ui.inputdialog.InputDialog;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.permission.FsAction;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class CreateDirectoryAction
        extends AnAction
{
    ConnectionHandler connectionHandler;
    FileSystemObject fileSystemObject;
    FileSystemObjectBundle fileSystemObjectBundle;
    public CreateDirectoryAction(ConnectionHandler connectionHandler,Object fileSystemObject)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.CREATEDIRECTORYACTION),"", Icons.ACTION_NEWDIR);
        this.connectionHandler=connectionHandler;
        getTemplatePresentation().setEnabled(connectionHandler.isConnected());
        if(fileSystemObject instanceof FileSystemObject)
        {
            this.fileSystemObject=(FileSystemObject) fileSystemObject;
            FsAction fsAction =HDFSUtil.getDirFileActionByUser(connectionHandler.getMainFileSystem(),
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
        else if(fileSystemObject instanceof FileSystemObjectBundle)
        {
            this.fileSystemObjectBundle = (FileSystemObjectBundle)fileSystemObject;
        }


    }

    public void actionPerformed(@NotNull AnActionEvent e)
    {
        if(ActionUtil.TestHdfsConnect(e.getProject(),connectionHandler))
        {
            String strPath = "";
            if (fileSystemObject != null)
            {
                strPath = fileSystemObject.getLocationString();
            } else
            {
                if (fileSystemObjectBundle != null)
                {
                    strPath = fileSystemObjectBundle.getLocationString();
                }
            }
            if (StringUtil.isEmptyOrSpaces(strPath)) return;
            InputDialog inputDialog = new InputDialog(e.getProject(),
                    LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.INPUTTEXT), "");
            inputDialog.show();
            String strName = inputDialog.getInputText();
            if (inputDialog.isOKAction())
            {
                if (StringUtil.isEmptyOrSpaces(strName))

                {
                    MessageUtil.showErrorDialog(e.getProject(),
                            LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                            LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.INPUTINFORMATION));
                    return;
                }
                if (!strPath.equals("/"))
                {
                    strPath = strPath + "/" + strName;
                } else
                {
                    strPath = strPath + strName;
                }
                try
                {
                    if (connectionHandler.getMainFileSystem().exists(new Path(strPath)))
                    {
                        MessageUtil.showErrorDialog(e.getProject(),
                                LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                                LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.INPUTFILEINFOR));
                        return;
                    }

                    createDir(connectionHandler.getMainFileSystem(),strPath,
                            connectionHandler.getSettings().getFileSystemSettings().getUser(),e.getProject());

                } catch (Exception ex)
                {

                }
                if (fileSystemObject != null)
                {
                    fileSystemObject.rebuildTreeChildren();
                } else
                {
                    if (fileSystemObjectBundle != null)
                    {
                        fileSystemObjectBundle.refreshTreeChildren();
                    }
                }
            }
        }
    }


    /**
     * 创建目录
     * @param fileSystem
     * @param strDirPath
     * @param strUserName
     * @param project
     * @throws IOException
     */
    public  void createDir(FileSystem fileSystem, String strDirPath, String strUserName, Project project) throws IOException
    {
        Path path = new Path(strDirPath);
        FsAction fsAction =HDFSUtil.getDirFileActionByUser(fileSystem,fileSystemObject.getLocationString(),strUserName);
        if(fsAction==FsAction.ALL||fsAction==FsAction.WRITE||fsAction==FsAction.WRITE_EXECUTE)
        {
            connectionHandler.getMainFileSystem().mkdirs(path);
        }
        else
        {
            MessageUtil.showInfoDialog(project, LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE),
                    "当前用户没有该目录的读写权限，无法创建目录");
        }
    }
}