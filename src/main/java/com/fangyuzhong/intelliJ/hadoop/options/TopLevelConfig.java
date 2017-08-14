package com.fangyuzhong.intelliJ.hadoop.options;

import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.core.options.ui.ConfigurationEditorForm;
import org.jetbrains.annotations.NotNull;

/**
 * 定义配置级别---顶级配置
 * @param <T>
 */
public interface TopLevelConfig <T extends ConfigurationEditorForm>
{
    /**
     * 获取配置的ID
     * @return
     */
    ConfigId getConfigId();

    /**
     * 获取原始配置项
     * @return
     */
    @NotNull
    Configuration<T> getOriginalSettings();
}
