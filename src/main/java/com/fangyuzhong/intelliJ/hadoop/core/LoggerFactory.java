package com.fangyuzhong.intelliJ.hadoop.core;

import com.intellij.openapi.diagnostic.Logger;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class LoggerFactory
{
    public static Logger createLogger()
    {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StackTraceElement stackTraceElement = stackTraceElements[0];
        for (int i = 0; i < stackTraceElements.length; i++)
        {
            if (stackTraceElements[i].getMethodName().equals("createLogger"))
            {
                stackTraceElement = i + 1 < stackTraceElements.length ? stackTraceElements[(i + 1)] : stackTraceElements[i];
                break;
            }
        }
        String className = stackTraceElement.getClassName();
        return Logger.getInstance(className);
    }

    public static Logger createLogger(Class clazz)
    {
        return Logger.getInstance(clazz.getName());
    }

    public static Logger createLogger(String name)
    {
        return Logger.getInstance(name);
    }
}