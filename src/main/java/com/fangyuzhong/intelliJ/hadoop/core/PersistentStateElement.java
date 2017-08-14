package com.fangyuzhong.intelliJ.hadoop.core;

/**
 * Created by fangyuzhong on 17-7-15.
 */
 public interface PersistentStateElement<T>
{
     void readState(T paramT);

     void writeState(T paramT);
}