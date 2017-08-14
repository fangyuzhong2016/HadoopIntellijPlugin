package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.progress.ProcessCanceledException;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class SimpleTask<T>
        extends AbstractTask<T>
{
    public void start()
    {
        run();
    }

    protected boolean canExecute()
    {
        return true;
    }

    public void run()
    {
        try
        {
            if (canExecute())
            {
                execute();
            } else
            {
                cancel();
            }
        } catch (ProcessCanceledException ignore)
        {
        }
    }

    protected abstract void execute();
}
