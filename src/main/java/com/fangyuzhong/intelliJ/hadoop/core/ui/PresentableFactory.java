package com.fangyuzhong.intelliJ.hadoop.core.ui;

import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleCallback;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class PresentableFactory<T extends Presentable>
{
    private String actionName;

    public PresentableFactory(String actionName)
    {
        this.actionName = actionName;
    }

    public String getActionName()
    {
        return this.actionName;
    }

    public abstract void create(SimpleCallback<T> paramSimpleCallback);
}
