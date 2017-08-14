package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.AlreadyDisposedException;
import com.fangyuzhong.intelliJ.hadoop.core.message.Message;
import com.fangyuzhong.intelliJ.hadoop.core.message.MessageBundle;
import com.fangyuzhong.intelliJ.hadoop.core.thread.RunnableTask;
import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class MessageUtil
{
    public static final String[] OPTIONS_OK = {"OK"};
    public static final String[] OPTIONS_YES_NO = {"Yes", "No"};

    public static void showErrorDialog(@Nullable Project project, String title, MessageBundle messages)
    {
        StringBuilder buffer = new StringBuilder();
        for (Message message : messages.getErrorMessages())
        {
            buffer.append(message.getText());
            buffer.append("\n");
        }
        showErrorDialog(project, title, buffer.toString());
    }

    public static void showErrorDialog(@Nullable Project project, String message, Exception exception)
    {
        showErrorDialog(project, null, message, exception);
    }

    public static void showErrorDialog(@Nullable Project project, String title, String message)
    {
        showErrorDialog(project, title, message, null);
    }

    public static void showErrorDialog(@Nullable Project project, String message)
    {
        showErrorDialog(project, null, message, null);
    }

    public static void showErrorDialog(@Nullable Project project, @Nullable String title, String message, @Nullable Exception exception)
    {
        if ((project != null) && (project.isDisposed()))
        {
            return;
        }
        if (exception != null)
        {
            if (exception == AlreadyDisposedException.INSTANCE)
            {
                return;
            }
            String exceptionMessage = exception.getMessage();
            if (exceptionMessage == null)
            {
                exceptionMessage = exception.getClass().getName();
            }
            message = message + "\n" + exceptionMessage.trim();
        }
        if (title == null)
        {
            title = "Error";
        }
        showDialog(project, message, title, OPTIONS_OK, 0, Icons.DIALOG_ERROR, null, null);
    }

    public static void showErrorDialog(@Nullable Project project, String title, String message, String[] options, int defaultOptionIndex, RunnableTask<Integer> callback)
    {
        showDialog(project, message, title, options, defaultOptionIndex, Icons.DIALOG_ERROR, callback, null);
    }

    public static void showQuestionDialog(@Nullable Project project, String title, String message, String[] options, int defaultOptionIndex, RunnableTask<Integer> callback)
    {
        showQuestionDialog(project, title, message, options, defaultOptionIndex, callback, null);
    }

    public static void showQuestionDialog(@Nullable Project project, String title, String message, String[] options, int defaultOptionIndex, RunnableTask<Integer> callback, @Nullable DialogWrapper.DoNotAskOption doNotAskOption)
    {
        showDialog(project, message, title, options, defaultOptionIndex, Icons.DIALOG_QUESTION, callback, doNotAskOption);
    }

    public static void showWarningDialog(@Nullable Project project, String title, String message)
    {
        showWarningDialog(project, title, message, OPTIONS_OK, 0, null);
    }

    public static void showWarningDialog(@Nullable Project project, String title, String message, String[] options, int defaultOptionIndex, RunnableTask<Integer> callback)
    {
        showDialog(project, message, title, options, defaultOptionIndex, Icons.DIALOG_WARNING, callback, null);
    }

    public static void showInfoDialog(@Nullable Project project, String title, String message)
    {
        showInfoDialog(project, title, message, OPTIONS_OK, 0, null);
    }

    public static void showInfoDialog(@Nullable Project project, String title, String message, String[] options, int defaultOptionIndex, RunnableTask<Integer> callback)
    {
        showDialog(project, message, title, options, defaultOptionIndex, Icons.DIALOG_INFORMATION, callback, null);
    }

    private static void showDialog(@Nullable Project project, final String message,
                                   final String title, final String[] options,
                                   final int defaultOptionIndex, final Icon icon,
                                   final RunnableTask<Integer> callback,
                                   @Nullable final DialogWrapper.DoNotAskOption doNotAskOption)
    {
        new SimpleLaterInvocator()
        {
            protected void execute()
            {
                int option = Messages.showDialog(project, message, "Hadoop Navigator - " + title, options, defaultOptionIndex, icon, doNotAskOption);
                if (callback != null)
                {
                    callback.setData(Integer.valueOf(option));
                    callback.start();
                }
            }
        }.start();
    }
}

