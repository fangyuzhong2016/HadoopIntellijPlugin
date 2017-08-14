package com.fangyuzhong.intelliJ.hadoop.core.thread;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class TaskInstructions
{
    private String title;
    private boolean startInBackground;
    private boolean canBeCancelled;

    public TaskInstructions(String title, boolean startInBackground, boolean canBeCancelled)
    {
        this.title = title;
        this.startInBackground = startInBackground;
        this.canBeCancelled = canBeCancelled;
    }

    public String getTitle()
    {
        return this.title;
    }

    public boolean isStartInBackground()
    {
        return this.startInBackground;
    }

    public boolean isCanBeCancelled()
    {
        return this.canBeCancelled;
    }
}

