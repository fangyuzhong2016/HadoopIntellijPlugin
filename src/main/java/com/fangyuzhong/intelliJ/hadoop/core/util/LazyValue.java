package com.fangyuzhong.intelliJ.hadoop.core.util;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public interface LazyValue<T>
{
     T get();

     void set(T paramT);

     boolean isLoaded();
}