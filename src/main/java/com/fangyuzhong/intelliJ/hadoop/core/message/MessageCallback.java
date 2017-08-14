package com.fangyuzhong.intelliJ.hadoop.core.message;

import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleTask;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class MessageCallback
        extends SimpleTask<Integer>
{
    private Integer executeOption;

    public MessageCallback()
    {
        setData(Integer.valueOf(0));
    }

    public MessageCallback(Integer executeOption)
    {
        this.executeOption = executeOption;
    }

    protected boolean canExecute()
    {
        return (this.executeOption == null) || (this.executeOption.equals(getData()));
    }
}