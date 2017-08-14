package com.fangyuzhong.intelliJ.hadoop.core.options.ui;

import com.fangyuzhong.intelliJ.hadoop.core.options.CompositeConfiguration;
import com.intellij.openapi.options.ConfigurationException;


/**
 * 定义配置集合的主窗体抽象类
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class CompositeConfigurationEditorForm<E extends CompositeConfiguration>
        extends ConfigurationEditorForm<E>
{
    protected CompositeConfigurationEditorForm(E configuration)
    {
        super(configuration);
    }

    public void applyFormChanges()
            throws ConfigurationException
    {
    }

    public void resetFormChanges()
    {
    }
}
