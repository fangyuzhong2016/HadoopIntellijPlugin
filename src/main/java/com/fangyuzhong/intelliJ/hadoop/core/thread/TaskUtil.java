package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public  class TaskUtil
{
    static void startTask(Task task, final Project project)
    {
        Application application = ApplicationManager.getApplication();
        if (application.isDispatchThread())
        {
            executeTask(task, project);
        } else
        {
            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    TaskUtil.executeTask(task, project);
                }
            };
            application.invokeLater(runnable, ModalityState.NON_MODAL);
        }
    }

    private static void executeTask(Task task, Project project)
    {
        if ((project == null) || (!project.isDisposed()))
        {
            ProgressManager progressManager = ProgressManager.getInstance();
            progressManager.run(task);
        }
    }
}
