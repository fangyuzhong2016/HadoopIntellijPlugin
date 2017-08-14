package com.fangyuzhong.intelliJ.hadoop.core.util;

import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class LazyThreadLocal<T>
        extends ThreadLocal<T>
{
    @NotNull
    public final T get()
    {
        T value = super.get();
        if (value == null)
        {
            value = load();
            set(value);
        }
        return value;
    }

    @NotNull
    protected abstract T load();
}
