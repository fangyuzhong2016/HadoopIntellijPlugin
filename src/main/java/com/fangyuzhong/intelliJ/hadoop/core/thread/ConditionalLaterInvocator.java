package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class ConditionalLaterInvocator<T>
        extends SynchronizedTask<T>
{
    public final void start()
    {
        Application application = ApplicationManager.getApplication();
        if (application.isDispatchThread())
        {
            run();
        } else
        {
            application.invokeLater(this);
        }
    }

    protected String getSyncKey()
    {
        return null;
    }
}
