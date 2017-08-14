package com.fangyuzhong.intelliJ.hadoop.fsbrowser.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.FileSystemBrowserTree;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.project.Project;

/**
 * Created by fangyuzhong on 17-7-22.
 */
public class CollapseTreeAction
        extends DumbAwareAction
{
    public CollapseTreeAction()
    {
        super("折叠", null, Icons.ACTION_COLLAPSE_ALL);
    }

    public void actionPerformed(AnActionEvent e)
    {
        Project project = ActionUtil.getProject(e);
        if (project != null)
        {
            FileSystemBrowserManager browserManager = FileSystemBrowserManager.getInstance(project);
            FileSystemBrowserTree activeBrowserTree = browserManager.getActiveBrowserTree();
            if (activeBrowserTree != null)
            {
                activeBrowserTree.collapseAll();
            }
        }
    }

    public void update(AnActionEvent e)
    {
        Presentation presentation = e.getPresentation();
        String text = LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.COLLAPSEALL);
        presentation.setText(text);
    }
}