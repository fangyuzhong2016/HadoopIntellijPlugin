package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class ConnectionSettingsAdapter
        implements ConnectionSettingsListener
{
    public void connectionsChanged()
    {
    }

    public void connectionChanged(String connectionId)
    {
    }

    public void connectionNameChanged(String connectionId)
    {
    }
}

