package com.fangyuzhong.intelliJ.hadoop.core.options.setting;

import com.fangyuzhong.intelliJ.hadoop.core.options.PersistentConfiguration;
import org.jdom.Element;

import javax.swing.*;

/**
 * 定义持久化Blooean类型的配置
 * Created by fangyuzhong on 17-7-21.
 */
public class BooleanSetting
        extends Setting<Boolean, JToggleButton>
        implements PersistentConfiguration
{
    public BooleanSetting(String name, Boolean value)
    {
        super(name, value);
    }

    public void readConfiguration(Element parent)
    {
        setValue(Boolean.valueOf(SettingsUtil.getBoolean(parent, getName(), ((Boolean) value()).booleanValue())));
    }

    public void readConfigurationAttribute(Element parent)
    {
        setValue(Boolean.valueOf(SettingsUtil.getBooleanAttribute(parent, getName(), ((Boolean) value()).booleanValue())));
    }

    public void writeConfiguration(Element parent)
    {
        SettingsUtil.setBoolean(parent, getName(), ((Boolean) value()).booleanValue());
    }

    public void writeConfigurationAttribute(Element parent)
    {
        SettingsUtil.setBooleanAttribute(parent, getName(), ((Boolean) value()).booleanValue());
    }

    public boolean to(JToggleButton checkBox)
    {
        return setValue(Boolean.valueOf(checkBox.isSelected()));
    }

    public void from(JToggleButton checkBox)
    {
        checkBox.setSelected(((Boolean) value()).booleanValue());
    }
}
