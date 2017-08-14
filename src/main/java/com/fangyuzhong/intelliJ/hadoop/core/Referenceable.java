package com.fangyuzhong.intelliJ.hadoop.core;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public interface Referenceable<R extends Reference>
{
     R getRef();

     String getName();
}