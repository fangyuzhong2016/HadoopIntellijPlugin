package com.fangyuzhong.intelliJ.hadoop.core.util;

/**
 * Created by fangyuzhong on 17-7-14.
 */

public abstract class RefreshableValue<T>
{
    private T value;
    private boolean loaded = false;
    private int refreshInterval;
    private long lastRefreshTimestamp;

    public RefreshableValue(int refreshInterval)
    {
        this.refreshInterval = refreshInterval;
    }

    public T get()
    {
        if ((!this.loaded) || (this.lastRefreshTimestamp < System.currentTimeMillis() - this.refreshInterval))
        {
            this.value = CommonUtil.nvln(load(), this.value);
            this.loaded = true;
            this.lastRefreshTimestamp = System.currentTimeMillis();
        }
        return (T) this.value;
    }

    protected abstract T load();
}