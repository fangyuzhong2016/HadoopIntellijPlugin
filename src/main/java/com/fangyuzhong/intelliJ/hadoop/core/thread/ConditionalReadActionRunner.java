package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.util.Computable;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class ConditionalReadActionRunner<T>
{
    public final T start()
    {
        Application application = ApplicationManager.getApplication();
        if (application.isReadAccessAllowed())
        {
            try
            {
                return (T) run();
            } catch (ProcessCanceledException e)
            {
                return null;
            }
        }
        Computable<T> readAction = new Computable()
        {
            public T compute()
            {
                try
                {
                    return (T) ConditionalReadActionRunner.this.run();
                } catch (ProcessCanceledException e)
                {
                }
                return null;
            }
        };
        return (T) ApplicationManager.getApplication().runReadAction(readAction);
    }

    protected abstract T run();
}
