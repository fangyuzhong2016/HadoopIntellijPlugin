package com.fangyuzhong.intelliJ.hadoop.fsobject.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.hadoop.fs.Path;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class ViewFileAction
        extends AnAction
{
    ConnectionHandler connectionHandler;
    FileSystemObject fileSystemObject;
    public ViewFileAction(ConnectionHandler connectionHandler,FileSystemObject fileSystemObject)
    {
        super(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.VIEWFILEACTION),"", Icons.ACTION_VIEWFILE);
        this.connectionHandler=connectionHandler;
        this.fileSystemObject=fileSystemObject;
    }
    public void actionPerformed(@NotNull AnActionEvent e)
    {
        try
        {
            String hdfsPath = fileSystemObject.getLocationString();
            String hdfsUrl = connectionHandler.getSettings().getFileSystemSettings().getHDFSUrl();
            int urllen = hdfsUrl.length();
            int hdfslen = hdfsPath.length();
            String subFilePath = hdfsPath.substring(hdfsUrl.length(),hdfsPath.length());
            subFilePath =subFilePath.substring(0,subFilePath.lastIndexOf('/'));
            if(!File.separator.equals("/"))
            {
                subFilePath.replace('/','\\');
            }
            String projectPath =e.getProject().getBasePath();
            String storDataDirPath = projectPath+"/data"+subFilePath;
            File file = new File(storDataDirPath);
            if(!file.exists())
            {
                file.mkdirs();
            }
            String filePath = storDataDirPath+File.separator+fileSystemObject.getName();
            if(!new File(filePath).exists())
            {
                connectionHandler.getMainFileSystem().copyToLocalFile(new Path(hdfsPath), new Path(storDataDirPath));
            }
           // String filePath ="/home/hadoopuser/HadoopIntellijPlugin-1.0.txt";
            FileEditorManager fileEditorManager = FileEditorManager.getInstance(e.getProject());
            VirtualFile vf = LocalFileSystem.getInstance().findFileByPath(filePath);
            fileEditorManager.openFile(vf, true, true);

        }
        catch (Exception ex)
        {

        }

    }
}
