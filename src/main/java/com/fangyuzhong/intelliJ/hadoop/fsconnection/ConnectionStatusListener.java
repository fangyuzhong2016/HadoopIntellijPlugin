package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * 定义连接状态监听接口
 * Created by fangyuzhong on 17-7-15.
 */
 public interface ConnectionStatusListener extends EventListener
{
    /**
     * 状态信息改变提示
     */
    public static final Topic<ConnectionStatusListener> TOPIC = Topic.create("文件系统连接状态改变", ConnectionStatusListener.class);

    /**
     * 状态改变事件
     * @param paramString
     */
     void statusChanged(String paramString);
}
