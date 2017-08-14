package com.fangyuzhong.intelliJ.hadoop.fsbrowser.option.listener;

import com.fangyuzhong.intelliJ.hadoop.fsbrowser.option.BrowserDisplayMode;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public  interface DisplayModeSettingsListener
        extends EventListener
{
     static final Topic<DisplayModeSettingsListener> TOPIC = Topic.create("Browser Display Mode Settings", DisplayModeSettingsListener.class);

      void displayModeChanged(BrowserDisplayMode paramBrowserDisplayMode);
}
