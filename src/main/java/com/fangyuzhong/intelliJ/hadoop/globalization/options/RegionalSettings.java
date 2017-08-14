package com.fangyuzhong.intelliJ.hadoop.globalization.options;

import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.core.options.setting.SettingsUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageSettingsListener;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.options.ui.RegionalSettingsEditorForm;
import com.fangyuzhong.intelliJ.hadoop.options.general.GeneralProjectSettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jdom.Element;

import java.util.Locale;

/**
 * 定义国际化语言设置类
 * Created by fangyuzhong on 17-8-1.
 */
public class RegionalSettings extends Configuration<RegionalSettingsEditorForm>
{
    private Locale locale = Locale.getDefault();
    private Project project=null;
    public void setProject(Project project)
    {
        this.project = project;
    }
    /**
     * 获取国际化语言配置实例对象
     * @param project
     * @return
     */
    public static RegionalSettings getInstance(Project project)
    {
        return GeneralProjectSettings.getInstance(project).getRegionalSettings();
    }
    /**
     * 应用
     * @throws ConfigurationException
     */
    @Override
    public void apply() throws ConfigurationException
    {
        super.apply();

    }

    /**
     * 获取当前配置的语言对象
     * @return
     */
    public Locale getLocale()
    {
        return locale;
    }

    /**
     * 设置当前国际化语言
     * @param locale
     */
    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }


    /*********************************************************
     *                      Configuration                    *
     *********************************************************/
    /**
     * 实例化国际化设置UI
     * @return
     */
    public RegionalSettingsEditorForm createConfigurationEditor()
    {
        return new RegionalSettingsEditorForm(this);
    }


    @Override
    public String getConfigElementName()
    {
        return "regional-settings";
    }

    /**
     * 读取配置，获取当前设置的国际化语言
     * @param element
     */
    public void readConfiguration(Element element)
    {
        String localeString = SettingsUtil.getString(element, "locale", Locale.getDefault().toString());
        boolean useSystemLocale = localeString.equals("SYSTEM_DEFAULT");
        if (useSystemLocale)
        {
            this.locale = Locale.getDefault();
        } else
        {
           this.locale = LocaleOption.getLocalOption(localeString).getLocale();
        }
        LocaleLanguageManager.getInstance().setResourceBundle(locale);
    }

    /**
     * 将当前的国际化配置写入配置文件
     * @param element
     */
    public void writeConfiguration(Element element)
    {
        String localLanguage ="SYSTEM_DEFAULT";
        if(locale!=null)
        {
            localLanguage = locale.getLanguage()+"-"+locale.getCountry();
        }
        SettingsUtil.setString(element, "locale", localLanguage);
    }
}
