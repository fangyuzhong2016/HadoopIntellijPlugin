package com.fangyuzhong.intelliJ.hadoop.core;

/**
 * Created by fangyuzhong on 17-7-14.
 */
 interface Reference<T extends Referenceable>
{
     T get();
}

