package com.fangyuzhong.intelliJ.hadoop.fsbrowser.option;

import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectType;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;

import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public  interface ObjectFilterChangeListener
        extends EventListener
{
     static final Topic<ObjectFilterChangeListener> TOPIC = Topic.create("Object filter changed", ObjectFilterChangeListener.class);

     void typeFiltersChanged(String paramString);

     void nameFiltersChanged(String paramString, @NotNull FileSystemObjectType... paramVarArgs);
}
