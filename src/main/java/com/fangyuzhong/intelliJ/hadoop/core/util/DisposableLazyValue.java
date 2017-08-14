package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.AlreadyDisposedException;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.Disposer;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class DisposableLazyValue<T>
        implements LazyValue<T>, Disposable
{
    private T value;
    private boolean loaded = false;
    private boolean disposed = false;

    public DisposableLazyValue(Disposable parent)
    {
        Disposer.register(parent, this);
    }

    public final T get()
    {
        if (this.disposed)
        {
            throw AlreadyDisposedException.INSTANCE;
        }
        if (!this.loaded)
        {
            synchronized (this)
            {
                if (this.disposed)
                {
                    throw AlreadyDisposedException.INSTANCE;
                }
                if (!this.loaded)
                {
                    this.value = load();
                    if ((this.value instanceof Disposable))
                    {
                        Disposer.register(this, (Disposable) this.value);
                    }
                    this.loaded = true;
                }
            }
        }
        return (T) this.value;
    }

    public final void set(T value)
    {
        this.value = value;
        this.loaded = (value != null);
    }

    public boolean isLoaded()
    {
        return this.loaded;
    }

    public final void reset()
    {
        set(null);
    }

    protected abstract T load();

    public void dispose()
    {
        this.disposed = true;
        this.value = null;
    }
}
