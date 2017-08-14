package com.fangyuzhong.intelliJ.hadoop.core.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class TextPresentable
        implements Presentable
{
    private String text;

    public TextPresentable(String text)
    {
        this.text = text;
    }

    @NotNull
    public String getName()
    {
        String tmp4_1 = this.text;
        if (tmp4_1 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/ui/TextPresentable", "getName"}));
        }
        return tmp4_1;
    }

    @Nullable
    public String getDescription()
    {
        return null;
    }

    @Nullable
    public Icon getIcon()
    {
        return null;
    }
}
