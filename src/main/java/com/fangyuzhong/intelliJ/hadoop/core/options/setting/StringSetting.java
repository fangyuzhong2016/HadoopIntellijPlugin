package com.fangyuzhong.intelliJ.hadoop.core.options.setting;

import com.fangyuzhong.intelliJ.hadoop.core.options.PersistentConfiguration;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.intellij.openapi.options.ConfigurationException;
import org.jdom.Element;

import javax.swing.text.JTextComponent;

/**
 * 定义持久化String类型的配置设置
 * Created by fangyuzhong on 17-7-21.
 */
public class StringSetting
        extends Setting<String, JTextComponent>
        implements PersistentConfiguration
{
    public StringSetting(String name, String value)
    {
        super(name, value);
    }

    public void readConfiguration(Element parent)
    {
        setValue(SettingsUtil.getString(parent, getName(), (String) value()));
    }

    public void writeConfiguration(Element parent)
    {
        SettingsUtil.setString(parent, getName(), (String) value());
    }

    public boolean to(JTextComponent component)
            throws ConfigurationException
    {
        return setValue(StringUtil.trim(component.getText()));
    }

    public void from(JTextComponent component)
    {
        component.setText((String) value());
    }
}