package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettingsListener;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-28.
 */
public interface OpenFileListener extends EventListener
{
    final Topic<OpenFileListener> TOPIC = Topic.create("Open File", OpenFileListener.class);

    void openFile(String filePath, Project project);
}

