package com.fangyuzhong.intelliJ.hadoop.core.options;

import com.fangyuzhong.intelliJ.hadoop.core.options.ui.CompositeConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.ui.GUIUtil;
import com.fangyuzhong.intelliJ.hadoop.options.TopLevelConfig;
import com.intellij.openapi.options.ConfigurationException;
import org.jdom.Element;
/**
 *定义配置集合类
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class CompositeConfiguration<T extends CompositeConfigurationEditorForm>
        extends Configuration<T>
{

    private Configuration[] configurations;

    /**
     * 获取配置集合类
     * @return
     */
    public final Configuration[] getConfigurations()
    {
        if (this.configurations == null)
        {
            this.configurations = createConfigurations();
        }
        return this.configurations;
    }

    /**
     * 创建配置集合
     * @return
     */
    protected abstract Configuration[] createConfigurations();

    /**
     * 是否修改
     * @return
     */
    public final boolean isModified()
    {
        for (Configuration configuration : getConfigurations())
        {
            if (configuration.isModified())
            {
                return true;
            }
        }
        return super.isModified();
    }

    /**
     * 应用程序
     * @throws ConfigurationException
     */
    public void apply()
            throws ConfigurationException
    {
        T settingsEditor = (T) getSettingsEditor();
        if (((this instanceof TopLevelConfig)) && (settingsEditor != null))
        {
            GUIUtil.stopTableCellEditing(settingsEditor.getComponent());
        }
        for (Configuration configuration : getConfigurations())
        {
            configuration.apply();
        }
        super.apply();
        onApply();
    }

    /**
     * 重置
     */
    public final void reset()
    {
        for (Configuration configuration : getConfigurations())
        {
            configuration.reset();
        }
        super.reset();
    }

    /**
     * 释放UI资源
     */
    public void disposeUIResources()
    {
        for (Configuration configuration : getConfigurations())
        {
            configuration.disposeUIResources();
        }
        super.disposeUIResources();
    }

    /**
     * 读取配置
     * @param element
     */
    public void readConfiguration(Element element)
    {
        Configuration[] configurations = getConfigurations();
        for (Configuration configuration : configurations)
        {
            readConfiguration(element,configuration);
        }
    }

    /**
     * 写入配置
     * @param element
     */
    public void writeConfiguration(Element element)
    {
        Configuration[] configurations = getConfigurations();
        for (Configuration configuration : configurations)
        {

            writeConfiguration(element,configuration);
        }
    }
}

