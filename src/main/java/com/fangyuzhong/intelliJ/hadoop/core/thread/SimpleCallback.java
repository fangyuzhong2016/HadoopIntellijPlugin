package com.fangyuzhong.intelliJ.hadoop.core.thread;

import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public interface SimpleCallback<T>
{
     void start(@Nullable T paramT);
}
