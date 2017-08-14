package com.fangyuzhong.intelliJ.hadoop.fsbrowser.option.listener;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public abstract interface ObjectDetailSettingsListener
        extends EventListener
{
    public static final Topic<ObjectDetailSettingsListener> TOPIC = Topic.create("Object Detail Settings", ObjectDetailSettingsListener.class);

    public abstract void displayDetailsChanged();
}