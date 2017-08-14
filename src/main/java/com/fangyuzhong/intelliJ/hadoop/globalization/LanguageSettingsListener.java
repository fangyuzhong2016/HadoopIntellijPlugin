package com.fangyuzhong.intelliJ.hadoop.globalization;

import com.intellij.util.messages.Topic;

import java.util.EventListener;
import java.util.Locale;

/**
 * 定义语言设置变化的监听
 * Created by fangyuzhong on 17-7-29.
 */
public interface LanguageSettingsListener
        extends EventListener
{
    Topic<LanguageSettingsListener> TOPIC = Topic.create("Language changed", LanguageSettingsListener.class);

    void LanguageChanged(Locale locale);
}
