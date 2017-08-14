package com.fangyuzhong.intelliJ.hadoop.core.thread;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class AbstractTask<T>
        implements RunnableTask<T>
{
    private T data;
    private boolean cancelled;

    public final T getData()
    {
        return (T) this.data;
    }

    public final void setData(T data)
    {
        this.data = data;
    }

    protected void cancel()
    {
        this.cancelled = true;
    }

    protected boolean isCancelled()
    {
        return this.cancelled;
    }
}