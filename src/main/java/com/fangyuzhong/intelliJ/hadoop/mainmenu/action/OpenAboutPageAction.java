package com.fangyuzhong.intelliJ.hadoop.mainmenu.action;

import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.mainmenu.AboutComponent;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-31.
 */
public class OpenAboutPageAction
        extends AnAction
{

    public OpenAboutPageAction()
    {
        super("Open About", null, null);
    }

    public void actionPerformed(AnActionEvent e)
    {
        Project project = ActionUtil.getProject(e);
        if (project != null)
        {
            AboutComponent aboutComponent = new AboutComponent(project);
            aboutComponent.showPopup(project);
        }
    }
    public void update(@NotNull AnActionEvent e)
    {
        Presentation presentation = e.getPresentation();
        presentation.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.ABOUT));
    }
}