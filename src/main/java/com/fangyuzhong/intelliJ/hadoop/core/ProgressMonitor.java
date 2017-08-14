package com.fangyuzhong.intelliJ.hadoop.core;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class ProgressMonitor
{
    public static String getTaskDescription()
    {
        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
        if (progressIndicator != null)
        {
            return progressIndicator.getText();
        }
        return null;
    }

    public static void setTaskDescription(String description)
    {
        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
        if (progressIndicator != null)
        {
            progressIndicator.setText(description);
        }
    }

    public static void setSubtaskDescription(String subtaskDescription)
    {
        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
        if (progressIndicator != null)
        {
            progressIndicator.setText2(subtaskDescription);
        }
    }

    public static boolean isCancelled()
    {
        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
        return progressIndicator != null && progressIndicator.isCanceled();
    }


}
