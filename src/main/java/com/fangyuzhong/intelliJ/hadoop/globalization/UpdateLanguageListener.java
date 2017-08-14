package com.fangyuzhong.intelliJ.hadoop.globalization;

import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * 定义修改界面语言的监听事件
 * Created by fangyuzhong on 17-7-29.
 */
public interface UpdateLanguageListener
        extends EventListener
{
    Topic<UpdateLanguageListener> TOPIC = Topic.create("Update Language", UpdateLanguageListener.class);

    void UpdateLanguage();
}
