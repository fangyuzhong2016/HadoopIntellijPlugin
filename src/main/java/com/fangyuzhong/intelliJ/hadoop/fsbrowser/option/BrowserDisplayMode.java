package com.fangyuzhong.intelliJ.hadoop.fsbrowser.option;

import com.fangyuzhong.intelliJ.hadoop.core.ui.Presentable;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-16.
 */

public enum BrowserDisplayMode
        implements Presentable
{
    SINGLE("Single tree"), SIMPLE("Single tree"), TABBED("Multiple tabs tree");

    private String name;

    private BrowserDisplayMode(String name)
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

    @Override
    public String toString()
    {
        String strShowName="";
        if(name.equals("Single tree"))
        {
            strShowName= LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SHOWMODELSINGLETREE);
        }
        else
        {
            strShowName= LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SHOWMODELMULTIPLETABTREE);
        }
        return strShowName;
    }
}
