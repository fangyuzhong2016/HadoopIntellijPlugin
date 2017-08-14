package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public interface ConnectionBundleSettingsListener extends EventListener
{
    Topic<ConnectionBundleSettingsListener> TOPIC = Topic.create("Connections changed", ConnectionBundleSettingsListener.class);

    void settingsChanged();
}

