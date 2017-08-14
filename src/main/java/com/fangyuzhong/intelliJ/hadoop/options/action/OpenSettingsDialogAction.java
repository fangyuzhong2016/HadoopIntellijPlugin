package com.fangyuzhong.intelliJ.hadoop.options.action;


import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.options.ConfigId;
import com.fangyuzhong.intelliJ.hadoop.options.ProjectSettingsManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class OpenSettingsDialogAction extends DumbAwareAction
{

    public void actionPerformed(@NotNull AnActionEvent e)
    {
        Project project = ActionUtil.getProject(e);
        if (project != null)
        {
            ProjectSettingsManager settingsManager = ProjectSettingsManager.getInstance(project);
            settingsManager.openProjectSettings(ConfigId.CONNECTIONS);
        }
    }

    public void update(@NotNull AnActionEvent e)
    {
        e.getPresentation().setText("Setup Connections...");
        e.getPresentation().setIcon(Icons.ACTION_EDIT);

    }

}
