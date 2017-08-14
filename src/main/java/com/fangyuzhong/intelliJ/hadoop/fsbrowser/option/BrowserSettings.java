package com.fangyuzhong.intelliJ.hadoop.fsbrowser.option;

import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.core.options.setting.SettingsUtil;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.option.ui.BrowserSettingEditorFrom;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.options.LocaleOption;
import com.fangyuzhong.intelliJ.hadoop.globalization.options.RegionalSettings;
import com.fangyuzhong.intelliJ.hadoop.globalization.options.ui.RegionalSettingsEditorForm;
import com.fangyuzhong.intelliJ.hadoop.options.general.GeneralProjectSettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import org.jdom.Element;

import java.util.Locale;

/**
 * Created by fangyuzhong on 17-8-8.
 */
public class BrowserSettings extends Configuration<BrowserSettingEditorFrom>
{
    private Project project=null;
    private BrowserDisplayMode browserDisplayMode=BrowserDisplayMode.TABBED;

    /**
     * 获取设置的树展示方式
     * @return
     */
    public BrowserDisplayMode getBrowserDisplayMode()
    {
        return browserDisplayMode;
    }

    public void setBrowserDisplayMode(BrowserDisplayMode browserDisplayMode)
    {
        this.browserDisplayMode = browserDisplayMode;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    public static BrowserSettings getInstance(Project project)
    {
        return GeneralProjectSettings.getInstance(project).getBrowserSettings();
    }

    @Override
    public void apply() throws ConfigurationException
    {
        super.apply();

    }

    /*********************************************************
     *                      Configuration                    *
     *********************************************************/
    public BrowserSettingEditorFrom createConfigurationEditor()
    {
        return new BrowserSettingEditorFrom(this);
    }

    @Override
    public String getConfigElementName()
    {
        return "browser-settings";
    }

    public void readConfiguration(Element element)
    {
        String browserDisplayModeString = SettingsUtil.getString(element,"browserdisplaymode",BrowserDisplayMode.TABBED.getName());
        boolean isSingleTree = browserDisplayModeString.equals("Single tree");
        if (isSingleTree)
        {
            this.browserDisplayMode=BrowserDisplayMode.SINGLE;
        }
        else
        {
            this.browserDisplayMode=BrowserDisplayMode.TABBED;
        }
    }

    public void writeConfiguration(Element element)
    {
        SettingsUtil.setString(element, "browserdisplaymode", browserDisplayMode.getName());
    }
}
