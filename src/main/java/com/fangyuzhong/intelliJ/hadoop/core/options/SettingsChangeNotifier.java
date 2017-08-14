package com.fangyuzhong.intelliJ.hadoop.core.options;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class SettingsChangeNotifier
{
    public SettingsChangeNotifier()
    {
        Configuration.registerChangeNotifier(this);
    }

    public abstract void notifyChanges();
}
