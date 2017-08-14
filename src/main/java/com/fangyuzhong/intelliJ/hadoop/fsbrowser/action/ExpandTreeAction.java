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
public class ExpandTreeAction
        extends DumbAwareAction
{
    public ExpandTreeAction()
    {
        super("展开", null, Icons.ACTION_EXPAND_ALL);
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
                activeBrowserTree.expandAll();
            }
        }
    }

    public void update(AnActionEvent e)
    {
        Presentation presentation = e.getPresentation();
        presentation.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.EXPANDALL));
    }
}