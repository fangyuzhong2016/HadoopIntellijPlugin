package com.fangyuzhong.intelliJ.hadoop.core.thread;

/**
 * Created by fangyuzhong on 17-7-14.
 */
 public interface RunnableTask<T>
        extends Runnable
{
     void start();

     void setData(T paramT);

     T getData();
}
