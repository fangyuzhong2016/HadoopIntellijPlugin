package com.fangyuzhong.intelliJ.hadoop.core.option;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.options.PersistentConfiguration;
import com.fangyuzhong.intelliJ.hadoop.core.options.setting.SettingsUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.Messages;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class InteractiveOptionHandler<T extends InteractiveOption>
        implements DialogWrapper.DoNotAskOption, PersistentConfiguration
{
    private String configName;
    private String title;
    private String message;
    private T defaultOption;
    private T selectedOption;
    private T lastUsedOption;
    private List<T> options;

    public InteractiveOptionHandler(String configName, String title, String message, @NotNull T defaultOption, T... options)
    {
        this.configName = configName;
        this.title = title;
        this.message = message;
        this.options = Arrays.asList(options);
        this.defaultOption = defaultOption;
    }

    public boolean isToBeShown()
    {
        return true;
    }

    public void setToBeShown(boolean keepAsking, int selectedIndex)
    {
        T selectedOption = getOption(selectedIndex);
        if ((keepAsking) || (selectedOption.isAsk()) || (selectedOption.isCancel()))
        {
            this.selectedOption = null;
        } else
        {
            this.selectedOption = selectedOption;
        }
    }

    public void set(T selectedOption)
    {
        assert (!selectedOption.isCancel());
        this.selectedOption = selectedOption;
    }

    @NotNull
    public T get()
    {
       return CommonUtil.nvl(this.selectedOption, this.defaultOption);
    }

    @NotNull
    public T getDefaultOption()
    {
        return this.defaultOption;
    }

    public boolean canBeHidden()
    {
        return true;
    }

    public boolean shouldSaveOptionsOnCancel()
    {
        return false;
    }

    @NotNull
    public String getDoNotShowMessage()
    {
        return "Remember option";
    }

    public T resolve(Object... messageArgs)
    {
        if ((this.selectedOption != null) && (!this.selectedOption.isAsk()))
        {
            return this.selectedOption;
        }
        int lastUsedOptionIndex = 0;
        if (this.lastUsedOption != null)
        {
            lastUsedOptionIndex = this.options.indexOf(this.lastUsedOption);
        }
        int optionIndex = Messages.showDialog(MessageFormat.format(this.message, messageArgs),
                "Hadoop Navigator - " + this.title, toStringOptions(this.options),
                lastUsedOptionIndex, Icons.DIALOG_QUESTION, this);

        T option = getOption(optionIndex);
        if ((!option.isCancel()) && (!option.isAsk()))
        {
            this.lastUsedOption = option;
        }
        return option;
    }

    @NotNull
    private T getOption(int index)
    {
        return index == -1 ? this.options.get(this.options.size() - 1) : this.options.get(index);
    }

    public static String[] toStringOptions(List<? extends InteractiveOption> options)
    {
        String[] stringOptions = new String[options.size()];
        for (int i = 0; i < options.size(); i++)
        {
            stringOptions[i] = options.get(i).getName();
        }
        return stringOptions;
    }

    public void readConfiguration(Element element)
    {
        T option = (T) SettingsUtil.getEnum(element, configName, (Enum) defaultOption);
        set(option);
    }

    public void writeConfiguration(Element element)
    {
        SettingsUtil.setEnum(element, this.configName, (Enum) get());
    }
}

