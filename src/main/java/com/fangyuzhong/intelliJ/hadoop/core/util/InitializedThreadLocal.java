package com.fangyuzhong.intelliJ.hadoop.core.util;

import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class InitializedThreadLocal<T>
        extends ThreadLocal<T>
{
    public InitializedThreadLocal(@NotNull T value)
    {
        set(value);
    }
}
