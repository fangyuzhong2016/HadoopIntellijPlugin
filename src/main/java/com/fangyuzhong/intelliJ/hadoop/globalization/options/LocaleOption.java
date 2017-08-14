package com.fangyuzhong.intelliJ.hadoop.globalization.options;

import com.fangyuzhong.intelliJ.hadoop.core.ui.Presentable;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

/**
 * Created by fangyuzhong on 17-7-29.
 */
public class LocaleOption
        implements Presentable
{
    public static List<LocaleOption> ALL = new ArrayList();
    private Locale locale;

    static
    {
        ALL.add(new LocaleOption(new Locale("en","US")));
        ALL.add(new LocaleOption(new Locale("zh","CN")));
        Collections.sort(ALL, new Comparator<LocaleOption>()
        {
            @Override
            public int compare(LocaleOption localeOption1, LocaleOption localeOption2)
            {
                return localeOption1.getName().compareTo(localeOption2.getName());
            }
        });
    }

    public LocaleOption(Locale locale)
    {
        this.locale = locale;
    }

    public Locale getLocale()
    {
        return this.locale;
    }

    @NotNull
    public String getName()
    {
        return (this.locale.equals(Locale.getDefault()) ? this.locale.getDisplayName() + " - System default" : this.locale.getDisplayName());
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

    @Nullable
    public static LocaleOption get(Locale locale)
    {
        for (LocaleOption localeOption : ALL)
        {
            if (localeOption.locale.equals(locale))
            {
                return localeOption;
            }
        }
        return null;
    }

    @Override
    public String toString()
    {
        return getName();
    }


    public static LocaleOption getLocalOption(String localLanguage)
    {
        if(StringUtil.isEmptyOrSpaces(localLanguage))
        {
            return new LocaleOption(new Locale("en","US"));
        }
        else
        {
            String[] strLocals = localLanguage.split("-");
            if(strLocals.length<2)
            {
                return new LocaleOption(new Locale("en","US"));
            }
            else
            {
                return new LocaleOption(new Locale(strLocals[0],strLocals[1]));
            }
        }
    }
}
