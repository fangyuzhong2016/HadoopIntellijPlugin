package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.AlreadyDisposedException;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.messages.MessageBus;
import com.intellij.util.messages.MessageBusConnection;
import com.intellij.util.messages.Topic;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class EventUtil
{
    private static MessageBusConnection connect(@Nullable Disposable parentDisposable)
    {
        MessageBus messageBus = ApplicationManager.getApplication().getMessageBus();
        return parentDisposable == null ? messageBus.connect() : messageBus.connect(parentDisposable);
    }

    private static MessageBusConnection connect(Project project, @Nullable Disposable parentDisposable)
    {
        MessageBus messageBus = project.getMessageBus();
        return parentDisposable == null ? messageBus.connect(project) : messageBus.connect(parentDisposable);
    }

    public static <T> void subscribe(Project project, @Nullable Disposable parentDisposable, Topic<T> topic, T handler)
    {
        if ((project != null) && (project != FailsafeUtil.DUMMY_PROJECT))
        {
            MessageBusConnection messageBusConnection = connect(project, parentDisposable);
            messageBusConnection.subscribe(topic, handler);
            //messageBusConnection.
        }
    }

    public static <T> void subscribe(@Nullable Disposable parentDisposable, Topic<T> topic, T handler)
    {
        MessageBusConnection messageBusConnection = connect(parentDisposable == null ? ApplicationManager.getApplication() : parentDisposable);
        messageBusConnection.subscribe(topic, handler);
    }

    @NotNull
    public static <T> T notify(@Nullable Project project, Topic<T> topic)
    {
        if ((project == null) || (project.isDisposed()) || (project == FailsafeUtil.DUMMY_PROJECT))
        {
            throw AlreadyDisposedException.INSTANCE;
        }
        MessageBus messageBus = project.getMessageBus();
        return messageBus.syncPublisher(topic);
    }
}