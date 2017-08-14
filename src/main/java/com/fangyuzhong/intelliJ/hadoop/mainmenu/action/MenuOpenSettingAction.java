package com.fangyuzhong.intelliJ.hadoop.mainmenu.action;

import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.options.ProjectSettingsManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-31.
 */
public class MenuOpenSettingAction extends AnAction
{
    public MenuOpenSettingAction()
    {
        super("setting", null,null);
    }

    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = ActionUtil.getProject(e);
        if (project != null)
        {

            FileSystemBrowserManager browserManager = FileSystemBrowserManager.getInstance(project);
            ConnectionHandler activeConnection = browserManager.getActiveConnection();
            ProjectSettingsManager.getInstance(project).openConnectionSettings(activeConnection);
        }
    }

    public void update(@NotNull AnActionEvent e)
    {
        Presentation presentation = e.getPresentation();
        presentation.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGTEXT));
    }
}