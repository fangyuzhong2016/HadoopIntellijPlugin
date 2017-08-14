package com.fangyuzhong.intelliJ.hadoop.options.ui;

import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.core.options.ui.CompositeConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.options.ui.ConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tab.TabbedPane;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionBundleSettings;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.ConnectionBundleSettingsForm;
import com.fangyuzhong.intelliJ.hadoop.options.ConfigId;
import com.fangyuzhong.intelliJ.hadoop.options.ProjectSettings;
import com.fangyuzhong.intelliJ.hadoop.options.general.GeneralProjectSettings;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ProjectSettingsEditorForm extends CompositeConfigurationEditorForm<ProjectSettings>
{
    private JPanel mainPanel;
    private JPanel tabsPanel;
    private JLabel newVersionLabel;
    private JPanel pluginUpdatePanel;
    private JPanel pluginUpdateLinkPanel;
    private TabbedPane configurationTabs;

    private ProjectSettingsDialog dialog;

    public ProjectSettingsEditorForm(ProjectSettings globalSettings)
    {
        super(globalSettings);
        configurationTabs = new TabbedPane(this);
        tabsPanel.add(configurationTabs, BorderLayout.CENTER);
        ConnectionBundleSettings connectionSettings = globalSettings.getConnectionSettings();
        final GeneralProjectSettings generalSettings = globalSettings.getGeneralSettings();
        addSettingsPanel(connectionSettings);
        addSettingsPanel(generalSettings);
        tabsPanel.setFocusable(false);
        newVersionLabel.setForeground(JBColor.DARK_GRAY);
        pluginUpdatePanel.setVisible(false);

        Disposer.register(this, configurationTabs);
    }

    public void setDialog(ProjectSettingsDialog dialog)
    {
        this.dialog = dialog;
    }

    private void addSettingsPanel(Configuration configuration)
    {
        JComponent component = configuration.createComponent();
        JBScrollPane scrollPane = new JBScrollPane(component);
        TabInfo tabInfo = new TabInfo(scrollPane);
        tabInfo.setText(configuration.getDisplayName());
        tabInfo.setObject(configuration);
        //tabInfo.setTabColor(GUIUtil.getWindowColor());
        configurationTabs.addTab(tabInfo);
    }

    public JComponent getComponent()
    {
        return mainPanel;
    }

    public void focusConnectionSettings(@Nullable ConnectionHandler connectionHandler)
    {
        ConnectionBundleSettings connectionSettings = getConfiguration().getConnectionSettings();
        ConnectionBundleSettingsForm settingsEditor = connectionSettings.getSettingsEditor();
        if (settingsEditor != null)
        {
            settingsEditor.selectConnection(connectionHandler);
            focusSettingsEditor(ConfigId.CONNECTIONS);
        }
    }

    public void focusSettingsEditor(ConfigId configId)
    {
        Configuration configuration = getConfiguration().getConfiguration(configId);
        if (configuration != null)
        {
            ConfigurationEditorForm settingsEditor = configuration.getSettingsEditor();
            if (settingsEditor != null)
            {
                JComponent component = settingsEditor.getComponent();
                if (component != null)
                {
                    TabInfo tabInfo = getTabInfo(component);
                    configurationTabs.select(tabInfo, true);
                }
            }
        }
    }

    private TabInfo getTabInfo(JComponent component)
    {
        for (TabInfo tabInfo : configurationTabs.getTabs())
        {
            JBScrollPane scrollPane = (JBScrollPane) tabInfo.getComponent();
            if (scrollPane.getViewport().getView() == component)
            {
                return tabInfo;
            }
        }
        return null;
    }

    @NotNull
    public Configuration getActiveSettings()
    {
        TabInfo tabInfo = configurationTabs.getSelectedInfo();
        if (tabInfo != null)
        {
            return (Configuration) tabInfo.getObject();
        }
        return getConfiguration();
    }

    public void dispose()
    {
        dialog = null;
        super.dispose();
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont)
    {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null)
        {
            resultName = currentFont.getName();
        } else
        {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1'))
            {
                resultName = fontName;
            } else
            {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setPreferredSize(new Dimension(900, 600));
        tabsPanel = new JPanel();
        tabsPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(tabsPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        pluginUpdatePanel = new JPanel();
        pluginUpdatePanel.setLayout(new GridLayoutManager(1, 2, new Insets(6, 6, 6, 6), -1, -1));
        mainPanel.add(pluginUpdatePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        newVersionLabel = new JLabel();
        newVersionLabel.setText("[new version available]");
        pluginUpdatePanel.add(newVersionLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        pluginUpdateLinkPanel = new JPanel();
        pluginUpdateLinkPanel.setLayout(new BorderLayout(0, 0));
        pluginUpdatePanel.add(pluginUpdateLinkPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}
