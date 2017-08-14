package com.fangyuzhong.intelliJ.hadoop.core.options;

import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposerUtil;
import com.fangyuzhong.intelliJ.hadoop.core.options.ui.ConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.thread.ConditionalLaterInvocator;
import com.fangyuzhong.intelliJ.hadoop.core.util.ThreadLocalFlag;
import com.fangyuzhong.intelliJ.hadoop.options.TopLevelConfig;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.progress.ProcessCanceledException;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 定义抽象配置基类
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class Configuration<T extends ConfigurationEditorForm>
        extends ConfigurationUtil
        implements SearchableConfigurable, PersistentConfiguration
{
    private static final Logger LOGGER =  LoggerFactory.createLogger();
    public static ThreadLocalFlag IS_RESETTING = new ThreadLocalFlag(false);
    public static ThreadLocal<List<SettingsChangeNotifier>> SETTINGS_CHANGE_NOTIFIERS = new ThreadLocal();
    private T configurationEditorForm;
    private boolean isModified = false;

    /**
     * 获取配置UI界面的帮助
     * @return
     */
    public String getHelpTopic()
    {
        return null;
    }

    /**
     *获取显示的名称
     * @return
     */
    @Nls
    public String getDisplayName()
    {
        return null;
    }

    /**
     *获取显示的图标
     * @return
     */
    public Icon getIcon()
    {
        return null;
    }

    /**
     *获取ID
     * @return
     */
    @NotNull
    public String getId()
    {
        return getClass().getName();
    }

    /**
     * 获取搜索的线程
     * @param option
     * @return
     */
    public Runnable enableSearch(String option)
    {
        return null;
    }

    /**
     * 获取设置的编辑主窗体
     * @return
     */
    @Nullable
    public final T getSettingsEditor()
    {
        return this.configurationEditorForm;
    }

    /**
     * 创建配置编辑主窗体UI
     * @return
     */
    @NotNull
    protected abstract T createConfigurationEditor();

    @NotNull
    public JComponent createComponent()
    {
        this.configurationEditorForm = createConfigurationEditor();
        return this.configurationEditorForm.getComponent();
    }

    public void setModified(boolean modified)
    {
        if (!isResetting().booleanValue())
        {
            this.isModified = modified;
        }
    }

    private static Boolean isResetting()
    {
        return Boolean.valueOf(IS_RESETTING.get());
    }

    public static void registerChangeNotifier(SettingsChangeNotifier notifier)
    {
        List<SettingsChangeNotifier> notifiers = (List) SETTINGS_CHANGE_NOTIFIERS.get();
        if (notifiers == null)
        {
            notifiers = new ArrayList();
            SETTINGS_CHANGE_NOTIFIERS.set(notifiers);
        }
        notifiers.add(notifier);
    }

    public boolean isModified()
    {
        return this.isModified;
    }

    /**
     * 应用
     * @throws ConfigurationException
     */
    public void apply()
            throws ConfigurationException
    {
        if ((this.configurationEditorForm != null) && (!this.configurationEditorForm.isDisposed()))
        {
            this.configurationEditorForm.applyFormChanges();
        }
        this.isModified = false;
        if ((this instanceof TopLevelConfig))
        {
            TopLevelConfig topLevelConfig = (TopLevelConfig) this;
            Configuration originalSettings = topLevelConfig.getOriginalSettings();
            if (originalSettings != this)
            {
                Element settingsElement = new Element("settings");
                writeConfiguration(settingsElement);
                originalSettings.readConfiguration(settingsElement);
            }
            notifyChanges();
        }
        onApply();
    }

    /**
     * 通知修改事件
     */
    public void notifyChanges()
    {
        List<SettingsChangeNotifier> changeNotifiers = (List) SETTINGS_CHANGE_NOTIFIERS.get();
        if (changeNotifiers != null)
        {
            SETTINGS_CHANGE_NOTIFIERS.set(null);
            for (SettingsChangeNotifier changeNotifier : changeNotifiers)
            {
                try
                {
                    changeNotifier.notifyChanges();
                } catch (Exception e)
                {
                    if (!(e instanceof ProcessCanceledException))
                    {
                        LOGGER.error("Error notifying configuration changes", e);
                    }
                }
            }
        }
    }

    @Deprecated
    protected void onApply()
    {
    }

    public void reset()
    {
        new ConditionalLaterInvocator()
        {
            protected void execute()
            {
                try
                {
                    if ((Configuration.this.configurationEditorForm != null) && (!Configuration.this.configurationEditorForm.isDisposed()))
                    {
                        Configuration.IS_RESETTING.set(true);
                        Configuration.this.configurationEditorForm.resetFormChanges();
                    }
                } finally
                {
                    Configuration.this.isModified = false;
                    Configuration.IS_RESETTING.set(false);
                }
            }
        }.start();
    }

    public void disposeUIResources()
    {
        DisposerUtil.dispose(this.configurationEditorForm);
        this.configurationEditorForm = null;
    }

    public String getConfigElementName()
    {
        return null;
    }

    protected static String nvl(String value)
    {
        return value == null ? "" : value;
    }
}
