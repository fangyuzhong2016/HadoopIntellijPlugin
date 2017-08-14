package com.fangyuzhong.intelliJ.hadoop.fsbrowser.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

/**
 * Created by fangyuzhong on 17-7-22.
 */
public class HideObjectPropertiesAction
        extends AnAction
{
    public HideObjectPropertiesAction()
    {
        super("隐藏对象属性", null, Icons.ACTION_CLOSE);
    }

    public void actionPerformed(AnActionEvent e)
    {
        Project project = ActionUtil.getProject(e);
        if (project != null)
        {
            FileSystemBrowserManager.getInstance(project).showObjectProperties(false);
        }
    }
}

