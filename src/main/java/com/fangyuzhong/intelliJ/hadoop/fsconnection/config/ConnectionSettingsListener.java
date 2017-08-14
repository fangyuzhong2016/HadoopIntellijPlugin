package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public  interface ConnectionSettingsListener
        extends EventListener
{
      Topic<ConnectionSettingsListener> TOPIC = Topic.create("Connection changed", ConnectionSettingsListener.class);

      void connectionsChanged();

      void connectionChanged(String paramString);

      void connectionNameChanged(String paramString);
}
