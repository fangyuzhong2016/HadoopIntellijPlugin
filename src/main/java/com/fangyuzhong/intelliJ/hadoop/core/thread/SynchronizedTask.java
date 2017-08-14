package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.progress.ProcessCanceledException;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class SynchronizedTask<T>
        extends SimpleTask<T>
{
    private static final SyncObjectProvider SYNC_OBJECT_PROVIDER = new SyncObjectProvider();

    public void start()
    {
        run();
    }

    public final void run()
    {
        try
        {
            if (canExecute())
            {
                String syncKey = getSyncKey();
                try
                {
                    Object syncObject = SYNC_OBJECT_PROVIDER.get(syncKey);
                    if (syncObject == null)
                    {
                        execute();
                    } else
                    {
                        synchronized (syncObject)
                        {
                            if (canExecute())
                            {
                                execute();
                            }
                        }
                    }
                } finally
                {
                    SYNC_OBJECT_PROVIDER.release(syncKey);
                }
            } else
            {
                cancel();
            }
        } catch (ProcessCanceledException ignore)
        {
        }
    }

    protected abstract String getSyncKey();
}

