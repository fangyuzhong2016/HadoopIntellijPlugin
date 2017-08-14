package com.fangyuzhong.intelliJ.hadoop.core.ui;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class MergedIcon
        implements Icon
{
    private final Icon myLeftIcon;
    private final int myHorizontalStrut;
    private final Icon myRightIcon;

    public MergedIcon(@NotNull Icon leftIcon, int horizontalStrut, @NotNull Icon rightIcon)
    {
        this.myLeftIcon = leftIcon;
        this.myHorizontalStrut = horizontalStrut;
        this.myRightIcon = rightIcon;
    }

    public void paintIcon(Component c, Graphics g, int x, int y)
    {
        paintIconAlignedCenter(c, g, x, y, this.myLeftIcon);
        paintIconAlignedCenter(c, g, x + this.myLeftIcon.getIconWidth() + this.myHorizontalStrut, y, this.myRightIcon);
    }

    private void paintIconAlignedCenter(Component c, Graphics g, int x, int y, @NotNull Icon icon)
    {
        if (icon == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"icon", "com/dci/intellij/dbn/core/ui/MergedIcon", "paintIconAlignedCenter"}));
        }
        int iconHeight = getIconHeight();
        icon.paintIcon(c, g, x, y + (iconHeight - icon.getIconHeight()) / 2);
    }

    public int getIconWidth()
    {
        return this.myLeftIcon.getIconWidth() + this.myHorizontalStrut + this.myRightIcon.getIconWidth();
    }

    public int getIconHeight()
    {
        return Math.max(this.myLeftIcon.getIconHeight(), this.myRightIcon.getIconHeight());
    }
}