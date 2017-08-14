package com.fangyuzhong.intelliJ.hadoop.core.util;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class SimpleLazyValue<T>
        implements LazyValue<T>
{
    private T value;

    public final T get()
    {
        if (this.value == null)
        {
            synchronized (this)
            {
                if (this.value == null)
                {
                    this.value = load();
                }
            }
        }
        return (T) this.value;
    }

    public void set(T value)
    {
        this.value = value;
    }

    public boolean isLoaded()
    {
        return this.value != null;
    }

    protected abstract T load();
}
