package com.fangyuzhong.intelliJ.hadoop.core.options.setting;

import com.fangyuzhong.intelliJ.hadoop.core.options.PersistentConfiguration;
import com.intellij.openapi.options.ConfigurationException;
import org.jdom.Element;

import javax.swing.*;

/**
 * 定义持久化Integer类型配置的设置
 * Created by fangyuzhong on 17-7-21.
 */
public class IntegerSetting
        extends Setting<Integer, JTextField>
        implements PersistentConfiguration
{
    public IntegerSetting(String name, Integer value)
    {
        super(name, value);
    }

    public void readConfiguration(Element parent)
    {
        setValue(Integer.valueOf(SettingsUtil.getInteger(parent, getName(), ((Integer) value()).intValue())));
    }

    public void writeConfiguration(Element parent)
    {
        SettingsUtil.setInteger(parent, getName(), ((Integer) value()).intValue());
    }

    public boolean to(JTextField component)
            throws ConfigurationException
    {
        return setValue(Integer.valueOf(Integer.parseInt(component.getText())));
    }

    public void from(JTextField component)
    {
        component.setText(((Integer) value()).toString());
    }
}
