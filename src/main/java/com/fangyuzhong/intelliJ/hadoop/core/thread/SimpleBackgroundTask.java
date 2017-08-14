package com.fangyuzhong.intelliJ.hadoop.core.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class SimpleBackgroundTask
        extends SynchronizedTask
{
    public static final ExecutorService POOL = Executors.newCachedThreadPool(new ThreadFactory()
    {
        public Thread newThread(@NotNull Runnable runnable)
        {
            if (runnable == null)
            {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"runnable", "com/dci/intellij/dbn/core/thread/SimpleBackgroundTask$1", "newThread"}));
            }
            Thread thread = new Thread(runnable, "DBN - Background Thread");
            thread.setPriority(1);
            thread.setDaemon(true);
            return thread;
        }
    });
    String name;

    public SimpleBackgroundTask(String name)
    {
        this.name = name;
    }

    public final void start()
    {
        POOL.submit(this);
    }

    protected String getSyncKey()
    {
        return null;
    }
}
