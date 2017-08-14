package com.fangyuzhong.intelliJ.hadoop.mainmenu.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-31.
 */
public class ShowHadoopToolWindowAction
        extends AnAction
{
    public ShowHadoopToolWindowAction()
    {
        super("ShowHadoopToolWindow", null, Icons.FILE_SYSTEM_HDFS);
    }

    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = ActionUtil.getProject(e);
        ToolWindow toolWindow = FileSystemBrowserManager.getInstance(project).getBrowserToolWindow();
        if(toolWindow!=null)
        {
            if(!toolWindow.isVisible())
            {
                toolWindow.show(null);
            }
            else
            {
                toolWindow.hide(null);
            }
        }
    }

    public void update(@NotNull AnActionEvent e)
    {
        Presentation presentation = e.getPresentation();
        presentation.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SYSTEMTEXT));
    }
}