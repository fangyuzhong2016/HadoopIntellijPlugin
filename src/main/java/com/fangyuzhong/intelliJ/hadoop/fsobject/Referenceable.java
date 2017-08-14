package com.fangyuzhong.intelliJ.hadoop.fsobject;

/**
 * Created by fangyuzhong on 17-7-15.
 */
 public interface Referenceable<R extends Reference>
{
     R getRef();

     String getName();
}
