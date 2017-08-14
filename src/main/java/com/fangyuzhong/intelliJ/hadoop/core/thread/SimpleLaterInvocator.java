package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.application.ApplicationManager;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class SimpleLaterInvocator
        extends SynchronizedTask
{
    public void start()
    {
        ApplicationManager.getApplication().invokeLater(this, ApplicationManager.getApplication().getDefaultModalityState());
    }

    protected String getSyncKey()
    {
        return null;
    }
}
