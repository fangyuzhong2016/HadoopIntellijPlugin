package com.fangyuzhong.intelliJ.hadoop.core;

/**
 * 定义计数器
 * Created by fangyuzhong on 17-7-14.
 */
public class Counter
{
    private int value;

    /**
     * 计数器的步长（增量）
     */
    public void increment()
    {
        this.value += 1;
        onIncrement();
    }

    /**
     * 减量
     */
    public void decrement()
    {
        this.value -= 1;
        onDecrement();
    }

    /**
     * 获取当前值
     * @return
     */
    public int getValue()
    {
        return this.value;
    }

    /**
     * 增量方法
     */
    public void onIncrement()
    {
    }

    /**
     * 减量方法
     */
    public void onDecrement()
    {
    }
}
