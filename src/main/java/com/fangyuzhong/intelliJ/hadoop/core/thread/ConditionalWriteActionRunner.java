package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.application.ApplicationManager;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class ConditionalWriteActionRunner
        extends WriteActionRunner
{
    public void start()
    {
        if (ApplicationManager.getApplication().isWriteAccessAllowed())
        {
            run();
        } else
        {
            super.start();
        }
    }
}
