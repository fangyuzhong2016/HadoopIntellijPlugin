package com.fangyuzhong.intelliJ.hadoop.core.util;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class ThreadLocalLazyValue<T>
        extends ThreadLocal<T>
{
    public final T get()
    {
        T value = super.get();
        if (value == null)
        {
            synchronized (this)
            {
                value = super.get();
                if (value == null)
                {
                    value = create();
                    set(value);
                }
            }
        }
        return value;
    }

    protected abstract T create();
}
