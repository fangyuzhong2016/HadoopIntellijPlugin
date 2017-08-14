package com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.options.ui.CompositeConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemHeaderForm;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tab.TabbedPane;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tab.TabbedPaneUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectivityStatus;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.FileSystemType;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionFileSystemSettings;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettings;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class ConnectionSettingsForm extends
        CompositeConfigurationEditorForm<ConnectionSettings>
{
    private JPanel mainPanel;
    private JPanel contentPanel;
    private JPanel headerPanel;
    private TabbedPane configTabbedPane;

    private FileSystemHeaderForm headerForm;

    public ConnectionSettingsForm(ConnectionSettings connectionSettings)
    {
        super(connectionSettings);
        ConnectionFileSystemSettings databaseSettings = connectionSettings.getFileSystemSettings();
        configTabbedPane = new TabbedPane(this);
        contentPanel.add(configTabbedPane, BorderLayout.CENTER);

        TabInfo connectionTabInfo = new TabInfo(new JBScrollPane(databaseSettings.createComponent()));
        connectionTabInfo.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGCONNECTIONTEXT));
        configTabbedPane.addTab(connectionTabInfo);

        ConnectivityStatus connectivityStatus = databaseSettings.getConnectivityStatus();
        Icon icon = connectionSettings.isNew() ? Icons.CONNECTION_NEW :
                !databaseSettings.isActive() ? Icons.CONNECTION_DISABLED :
                        connectivityStatus == ConnectivityStatus.VALID ? Icons.CONNECTION_ACTIVE :
                                connectivityStatus == ConnectivityStatus.INVALID ? Icons.CONNECTION_INVALID : Icons.CONNECTION_INACTIVE;

        headerForm = new FileSystemHeaderForm(connectionSettings.getFileSystemSettings().getName(), icon, null);
        headerPanel.add(headerForm.getComponent(), BorderLayout.CENTER);
        EventUtil.subscribe(databaseSettings.getProject(), ConnectionPresentationChangeListener.TOPIC, connectionPresentationChangeListener);
    }

    public void selectTab(String tabName)
    {
        TabbedPaneUtil.selectTab(configTabbedPane, tabName);
    }

    public String getSelectedTabName()
    {
        return TabbedPaneUtil.getSelectedTabName(configTabbedPane);
    }

    public JComponent getComponent()
    {
        return mainPanel;
    }

    /**
     * 添加一个连接后，的监听
     */
    ConnectionPresentationChangeListener connectionPresentationChangeListener = new ConnectionPresentationChangeListener()
    {
        @Override
        public void presentationChanged(final String name, final Icon icon, final Color color, final String connectionId, FileSystemType fileSystemType)
        {
            new SimpleLaterInvocator()
            {

                @Override
                public void execute()
                {
                    ConnectionSettings configuration = getConfiguration();
                    if (configuration != null && configuration.getConnectionId().equals(connectionId))
                    {
                        if (name != null) headerForm.setTitle(name);
                        if (icon != null) headerForm.setIcon(icon);
                        if (color != null) headerForm.setBackground(color);
                        else headerForm.setBackground(UIUtil.getPanelBackground());
                        //if (fileSystemType != null) databaseIconLabel.setIcon(fileSystemType.getLargeIcon());
                    }
                }
            }.start();
        }

    };


    @Override
    public void dispose()
    {
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
        headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(headerPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(contentPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}

