package com.fangyuzhong.intelliJ.hadoop.options.general;


import com.fangyuzhong.intelliJ.hadoop.core.options.CompositeProjectConfiguration;
import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.option.BrowserSettings;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.options.RegionalSettings;
import com.fangyuzhong.intelliJ.hadoop.options.ConfigId;
import com.fangyuzhong.intelliJ.hadoop.options.ProjectSettingsManager;
import com.fangyuzhong.intelliJ.hadoop.options.TopLevelConfig;
import com.fangyuzhong.intelliJ.hadoop.options.general.ui.GeneralProjectSettingsForm;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * 定义一般配置项设置，包括国际化语言设置和浏览器树展现方式设置
 */
public class GeneralProjectSettings extends CompositeProjectConfiguration<GeneralProjectSettingsForm>
        implements TopLevelConfig
{
    private RegionalSettings regionalSettings;
    private BrowserSettings browserSettings;

    /**
     * 初始化
     * @param project
     */
    public GeneralProjectSettings(Project project)
    {
        super(project);
        //区域语言设置
        regionalSettings = new RegionalSettings();
        regionalSettings.setProject(project);
        //HDFS文件系统浏览器树展现方式设置
        browserSettings = new BrowserSettings();
        browserSettings.setProject(project);
    }
    @NotNull
    @Override
    public String getId()
    {
        return "HadoopNavigator.Project.GeneralSettings";
    }

    /**
     * 获取显示名称
     * @return
     */
    public String getDisplayName()
    {
        return LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGGENERALTILE);
    }

    /**
     * 获取配置ID
     * @return
     */
    @Override
    public ConfigId getConfigId()
    {
        return ConfigId.GENERAL;
    }

    /**
     * 获取原始通用配置
     * @return
     */
    @Override
    public Configuration<GeneralProjectSettingsForm> getOriginalSettings()
    {
        return getInstance(getProject());
    }

    /**
     * 获取通用配置
     * @param project
     * @return
     */
    public static GeneralProjectSettings getInstance(Project project)
    {
        return ProjectSettingsManager.getSettings(project).getGeneralSettings();
    }

    /**
     * 获取语言设置配置
     * @return
     */
    public RegionalSettings getRegionalSettings()
    {
        return regionalSettings;
    }

    /**
     * 获取HDFS浏览器树展现方式配置
     * @return
     */
    public BrowserSettings getBrowserSettings()
    {
        return browserSettings;
    }

    /*********************************************************
     *                      Configuration                    *
     *********************************************************/
    /**
     * 创建通用配置编辑UI
     * @return
     */
    public GeneralProjectSettingsForm createConfigurationEditor()
    {
        return new GeneralProjectSettingsForm(this);
    }

    @Override
    public String getConfigElementName()
    {
        return "general-settings";
    }

    /**
     * 获取通用配置集合
     * @return
     */
    protected Configuration[] createConfigurations()
    {
        return new Configuration[]{regionalSettings,browserSettings};
    }

}
