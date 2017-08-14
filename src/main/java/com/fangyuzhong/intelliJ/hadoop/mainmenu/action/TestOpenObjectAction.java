package com.fangyuzhong.intelliJ.hadoop.mainmenu.action;

import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-8-2.
 */
public class TestOpenObjectAction
        extends AnAction
{

    public TestOpenObjectAction()
    {
        super("Open Object", null, null);
    }

    public void actionPerformed(AnActionEvent e)
    {
        Project project = ActionUtil.getProject(e);
        if (project == null) return;
        //打开Object
//        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
//        VirtualFile virtualFile = new DBConnectionVirtualFile(ConnectionManager.getInstance(project).getConnectionHandlers().get(0));
//        //VirtualFile vfs = new TestVirtualFile("test","dsdsdsdsd",100);
//        fileEditorManager.openFile(virtualFile, true);

    }
    public void update(@NotNull AnActionEvent e)
    {
    }
}