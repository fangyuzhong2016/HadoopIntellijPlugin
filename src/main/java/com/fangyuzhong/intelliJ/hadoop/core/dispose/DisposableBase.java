package com.fangyuzhong.intelliJ.hadoop.core.dispose;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class DisposableBase
        implements Disposable
{
    private boolean disposed;

    public  boolean isDisposed()
    {
        return this.disposed;
    }

    public void dispose()
    {
        this.disposed = true;
    }
}
