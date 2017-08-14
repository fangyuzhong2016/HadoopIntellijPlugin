package com.fangyuzhong.intelliJ.hadoop.fsconnection;

/**
 * 定义连接类型
 * Created by fangyuzhong on 17-7-15.
 */
public enum ConnectionType
{
    /**
     * 主连接
     */
    MAIN("Main"),
    /**
     * 连接池
     */
    POOL("Pool"),
    /**
     * 测试连接
     */
    TEST("Test");

    private String name;

    private ConnectionType(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }
}