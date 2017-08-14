package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public interface ConnectionLoadListener
        extends EventListener
{
    public static final Topic<ConnectionLoadListener> TOPIC = Topic.create("meta-data load event", ConnectionLoadListener.class);

     void contentsLoaded(ConnectionHandler paramConnectionHandler);
}
