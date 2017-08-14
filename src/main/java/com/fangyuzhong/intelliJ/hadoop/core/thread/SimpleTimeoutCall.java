package com.fangyuzhong.intelliJ.hadoop.core.thread;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.*;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class SimpleTimeoutCall<T>
        implements Callable<T>
{
    public static final ExecutorService POOL = Executors.newCachedThreadPool(new ThreadFactory()
    {
        public Thread newThread(@NotNull Runnable runnable)
        {
            if (runnable == null)
            {
                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"runnable", "com/dci/intellij/dbn/core/thread/SimpleTimeoutCall$1", "newThread"}));
            }
            Thread thread = new Thread(runnable, "DBN - Timed-out Execution Thread");
            thread.setPriority(1);
            thread.setDaemon(true);
            return thread;
        }
    });
    private long timeout;
    private TimeUnit timeoutUnit;
    private T defaultValue;

    public SimpleTimeoutCall(long timeout, TimeUnit timeoutUnit, T defaultValue)
    {
        this.timeout = timeout;
        this.timeoutUnit = timeoutUnit;
        this.defaultValue = defaultValue;
    }

    public final T start()
    {
        try
        {
            Future<T> future = POOL.submit(this);
            return (T) future.get(this.timeout, this.timeoutUnit);
        } catch (Exception e)
        {
            return (T) handleException(e);
        }
    }

    protected T handleException(Exception e)
    {
        return (T) this.defaultValue;
    }
}