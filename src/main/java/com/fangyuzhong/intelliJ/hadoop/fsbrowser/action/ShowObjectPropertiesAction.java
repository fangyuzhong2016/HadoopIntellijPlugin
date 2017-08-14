package com.fangyuzhong.intelliJ.hadoop.fsbrowser.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-22.
 */
public class ShowObjectPropertiesAction
        extends ToggleAction
        implements DumbAware
{
    public ShowObjectPropertiesAction()
    {
        super("showobject", null, Icons.BROWSER_OBJECT_PROPERTIES);
    }

    public boolean isSelected(AnActionEvent e)
    {
        Project project = ActionUtil.getProject(e);
        return (project != null)&&(FileSystemBrowserManager.getInstance(project).getObjectIsShow());
    }

    public void setSelected(AnActionEvent e, boolean state)
    {
        Project project = ActionUtil.getProject(e);
        if (project != null)
        {
            FileSystemBrowserManager.getInstance(project).showObjectProperties(state);
        }
    }

    public void update(@NotNull AnActionEvent e)
    {
        if (e == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"e", "com/dci/intellij/dbn/browser/action/ShowObjectPropertiesAction", "update"}));
        }
        super.update(e);
        e.getPresentation().setText(isSelected(e) ? LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.HIDEOBJECTPROPERTIES)
                : LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SHOWOBJECTPROPERTIES));
    }
}
