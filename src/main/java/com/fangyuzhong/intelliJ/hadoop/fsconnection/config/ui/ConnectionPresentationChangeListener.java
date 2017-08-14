package com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.FileSystemType;
import com.intellij.util.messages.Topic;

import javax.swing.*;
import java.awt.*;
import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public  interface ConnectionPresentationChangeListener
        extends EventListener
{
     static final Topic<ConnectionPresentationChangeListener> TOPIC = Topic.create("Connection presentation changed", ConnectionPresentationChangeListener.class);

      void presentationChanged(String paramString1, Icon paramIcon,
                               Color paramColor, String paramString2, FileSystemType paramFileSystemType);
}

