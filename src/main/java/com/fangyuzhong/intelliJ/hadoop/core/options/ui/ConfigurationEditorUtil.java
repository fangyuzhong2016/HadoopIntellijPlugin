package com.fangyuzhong.intelliJ.hadoop.core.options.ui;

import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.intellij.openapi.options.ConfigurationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class ConfigurationEditorUtil
{
    public static int validateIntegerInputValue(@NotNull JTextField inputField, @NotNull String name, boolean required, int min, int max, @Nullable String hint)
            throws ConfigurationException
    {
        try
        {
            String value = inputField.getText();
            if ((required) && (StringUtil.isEmpty(value)))
            {
                String message = "Input value for \"" + name + "\" must be specified";
                throw new ConfigurationException(message, "Invalid config value");
            }
            if (StringUtil.isNotEmpty(value))
            {
                int integer = Integer.parseInt(value);
                if ((min > integer) || (max < integer))
                {
                    throw new NumberFormatException("Number not in range");
                }
                return integer;
            }
            return 0;
        }
        catch (NumberFormatException e)
        {
            inputField.grabFocus();
            inputField.selectAll();
            String message = "Input value for \"" + name + "\" must be an integer between " + min + " and " + max + ".";
            if (hint != null)
            {
                message = message + " " + hint;
            }
            throw new ConfigurationException(message, "Invalid config value1");
        }
    }

    public static String validateStringInputValue(@NotNull JTextField inputField, @NotNull String name, boolean required)
            throws ConfigurationException
    {
        String value = inputField.getText().trim();
        if ((required) && (value.length() == 0))
        {
            String message = "Input value for \"" + name + "\" must be specified";
            throw new ConfigurationException(message, "Invalid config value");
        }
        return value;
    }
}