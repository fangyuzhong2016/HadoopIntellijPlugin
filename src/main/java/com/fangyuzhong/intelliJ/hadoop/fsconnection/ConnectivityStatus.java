package com.fangyuzhong.intelliJ.hadoop.fsconnection;

/**
 * 定义连接状态是否有效
 * Created by fangyuzhong on 17-7-15.
 */
public enum ConnectivityStatus
{
    /**
     * 连接有效
     */
    VALID,
    /**
     * 连接无效
     */
    INVALID,
    /**
     * 未知
     */
    UNKNOWN;

    private ConnectivityStatus()
    {
    }
}
