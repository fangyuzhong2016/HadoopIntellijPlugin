package com.fangyuzhong.intelliJ.hadoop.core;

import com.fangyuzhong.intelliJ.hadoop.core.util.TimeUtil;
import com.intellij.openapi.util.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class LoadIcon
        implements Icon
{
    public static final Icon INSTANCE = new LoadIcon();
    private static Icon[] icons = new Icon[12];
    private static int iconIndex;

    static
    {
        for (int i = 0; i <= 11; i++)
        {
            icons[i] = IconLoader.getIcon("/process/step_" + (i + 1) + ".png");
        }
    }

    private static long lastAccessTimestamp = System.currentTimeMillis();
    private static java.util.Timer ICON_ROLLER;

    private static class IconRollerTimerTask
            extends TimerTask
    {
        @Override
        public void run()
        {
            iconIndex++;
            if (iconIndex == icons.length) iconIndex = 0;
            if (ICON_ROLLER != null && TimeUtil.isOlderThan(lastAccessTimestamp, TimeUtil.TEN_SECONDS))
            {
                synchronized (IconRollerTimerTask.class)
                {
                    java.util.Timer cachedIconRoller = ICON_ROLLER;
                    ICON_ROLLER = null;
                    cachedIconRoller.purge();
                    cachedIconRoller.cancel();
                }
            }
        }
    }

    private static void startRoller()
    {
        if (ICON_ROLLER == null)
        {
            synchronized (IconRollerTimerTask.class)
            {
                if (ICON_ROLLER == null)
                {
                    ICON_ROLLER = new java.util.Timer("Hadoop - Load in Progress (icon roller)");
                    ICON_ROLLER.schedule(new IconRollerTimerTask(), 50L, 50L);
                }
            }
        }
    }

    private static Icon getIcon()
    {
        startRoller();
        lastAccessTimestamp = System.currentTimeMillis();
        return icons[iconIndex];
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        getIcon().paintIcon(c, g, x, y);
    }

    public int getIconWidth()
    {
        return getIcon().getIconWidth();
    }

    public int getIconHeight()
    {
        return getIcon().getIconHeight();
    }
}