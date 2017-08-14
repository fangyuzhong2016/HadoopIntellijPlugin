package com.fangyuzhong.intelliJ.hadoop.core.ui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 定义界面显示对象
 * Created by fangyuzhong on 17-7-14.
 */
 public interface Presentable
{
    /**
     * 显示未知对象
     */
    public static final Presentable UNKNOWN = new Presentable()
    {
        @NotNull
        public String getName()
        {
            return "Unknown";
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
    };

    /**
     * 获取显示名称
     *
     * @return
     */
    @NotNull
    String getName();

    /**
     * 获取显示描述
     *
     * @return
     */
    @Nullable
    String getDescription();

    /**
     * 获取显示图标
     *
     * @return
     */
    @Nullable
    Icon getIcon();
}
