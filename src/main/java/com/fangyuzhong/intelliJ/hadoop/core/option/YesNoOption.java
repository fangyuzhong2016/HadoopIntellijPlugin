package com.fangyuzhong.intelliJ.hadoop.core.option;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public enum YesNoOption
        implements InteractiveOption
{
    YES("Yes", true), NO("No", true);

    private String name;
    private boolean persistable;

    private YesNoOption(String name, boolean persistable)
    {
        this.name = name;
        this.persistable = persistable;
    }

    @NotNull
    public String getName()
    {
        return this.name;
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

    public boolean isCancel()
    {
        return false;
    }

    public boolean isAsk()
    {
        return false;
    }

    public static YesNoOption get(String name)
    {
        for (YesNoOption option :values())
        {
            if ((option.name.equals(name)) || (option.name().equals(name)))
            {
                return option;
            }
        }
        return null;
    }
}
