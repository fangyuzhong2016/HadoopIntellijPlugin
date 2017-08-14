package com.fangyuzhong.intelliJ.hadoop.fsobject;

/**
 *
 * Created by fangyuzhong on 17-7-15.
 */
public  interface Reference<T extends Referenceable>
{
     T get();
}

