package com.fangyuzhong.intelliJ.hadoop.fsconnection;

/**
 * 定义文件系统连接状态
 * Created by fangyuzhong on 17-7-15.
 */
public class ConnectionStatus
{
    private boolean connected = false;
    private boolean valid = true;
    private boolean resolvingIdleStatus;
    private String statusMessage;

    /**
     * 获取是否连接
     * @return
     */
    public boolean isConnected()
    {
        return this.connected;
    }

    /**
     * 设置是连接
     * @param connected
     */
    public void setConnected(boolean connected)
    {
        this.connected = connected;
    }

    /**
     * 获取是否验证
     * @return
     */
    public boolean isValid()
    {
        return this.valid;
    }

    /**
     * 设置验证
     * @param valid
     */
    public void setValid(boolean valid)
    {
        this.valid = valid;
    }

    /**
     * 获取连接状态信息
     * @return
     */
    public String getStatusMessage()
    {
        return this.statusMessage;
    }

    /**
     * 设置连接状态信息
     * @param statusMessage
     */
    public void setStatusMessage(String statusMessage)
    {
        this.statusMessage = statusMessage;
    }

    /**
     *获取是否正在解析连接状态
     * @return
     */
    public boolean isResolvingIdleStatus()
    {
        return this.resolvingIdleStatus;
    }

    /**
     *设置是否正在解析连接状态
     * @param resolvingIdleStatus
     */
    public void setResolvingIdleStatus(boolean resolvingIdleStatus)
    {
        this.resolvingIdleStatus = resolvingIdleStatus;
    }


}
