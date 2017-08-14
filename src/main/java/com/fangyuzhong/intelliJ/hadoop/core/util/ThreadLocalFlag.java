package com.fangyuzhong.intelliJ.hadoop.core.util;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class ThreadLocalFlag
{
    private boolean defaultValue;
    private ThreadLocal<Boolean> flag = new ThreadLocal();

    public ThreadLocalFlag(boolean defaultValue)
    {
        this.defaultValue = defaultValue;
    }

    public boolean get()
    {
        Boolean value = (Boolean) this.flag.get();
        return value == null ? this.defaultValue : value.booleanValue();
    }

    public void set(boolean value)
    {
        this.flag.set(Boolean.valueOf(value));
    }
}
