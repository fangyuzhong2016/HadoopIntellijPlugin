package com.fangyuzhong.intelliJ.hadoop.core.options.setting;

import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.intellij.openapi.options.ConfigurationException;

/**
 * 定义抽象设置类
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class Setting<T, E>
{
    private T value;
    private String name;

    protected Setting(String configName, T value)
    {
        this.name = configName;
        this.value = value;
    }

    /**
     * 配置项名称
     * @return
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * 配置项的值
     * @return
     */
    public T value()
    {
        return  this.value;
    }

    /**
     * 设置配置项的值
     * @param value
     * @return
     */
    public boolean setValue(T value)
    {
        boolean response = !CommonUtil.safeEqual(this.value, value);
        this.value = value;
        return response;
    }

    /**
     * 获取配置项的值
     * @return
     */
    public T getValue()
    {
        return  this.value;
    }

    /**
     * 获取Setting的返回值
     * @return
     */
    public String toString()
    {
        return "[" + getClass().getSimpleName() + "] " + this.name + " = " + this.value;
    }

    /**
     *
     * @param paramE
     * @return
     * @throws ConfigurationException
     */
    public abstract boolean to(E paramE)
            throws ConfigurationException;

    /**
     *
     * @param paramE
     */
    public abstract void from(E paramE);
}
