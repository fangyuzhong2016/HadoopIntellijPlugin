package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.ui.Presentable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public enum FileSystemUrlType
        implements Presentable
{

    DATABASE("Database"),
    FILE("File");

    private String name;

    private FileSystemUrlType(String name)
    {
        this.name = name;
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
}
