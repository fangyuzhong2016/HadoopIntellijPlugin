package com.fangyuzhong.intelliJ.hadoop.core.content;

import com.fangyuzhong.intelliJ.hadoop.core.util.ThreadLocalFlag;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class DatabaseLoadMonitor
{
    private static ThreadLocalFlag loadingInBackground = new ThreadLocalFlag(false);
    private static ThreadLocalFlag ensureDataLoaded = new ThreadLocalFlag(true);

    public static boolean isLoadingInBackground()
    {
        return loadingInBackground.get();
    }

    public static void startBackgroundLoad()
    {
        loadingInBackground.set(true);
    }

    public static void endBackgroundLoad()
    {
        loadingInBackground.set(false);
    }

    public static boolean isEnsureDataLoaded()
    {
        return ensureDataLoaded.get();
    }

    public static void setEnsureDataLoaded(boolean value)
    {
        ensureDataLoaded.set(value);
    }
}