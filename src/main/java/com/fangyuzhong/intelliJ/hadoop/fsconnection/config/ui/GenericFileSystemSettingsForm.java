package com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.options.SettingsChangeNotifier;
import com.fangyuzhong.intelliJ.hadoop.core.options.ui.ConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.options.ui.ConfigurationEditorUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectivityStatus;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionBundleSettings;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionFileSystemSettings;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettingsListener;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.GenericConnectionFileSystemSettings;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.DocumentAdapter;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

import static com.fangyuzhong.intelliJ.hadoop.core.ui.GUIUtil.updateBorderTitleForeground;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class GenericFileSystemSettingsForm extends
        ConfigurationEditorForm<GenericConnectionFileSystemSettings>
{
    private JButton testButton;
    private JButton infoButton;
    private JPanel mainPanel;
    private JTextField nameTextField;
    private JTextField descriptionTextField;
    private JTextField userTextField;
    private JTextField hdfsHostTextField;
    private JPasswordField passwordField;
    private JCheckBox osAuthenticationCheckBox;
    private JCheckBox activeCheckBox;
    private JPanel connectionParametersPanel;
    private JTextField mapReduceHostTextField;
    private JTextField mapReducePortTextField;
    private JTextField hdfsPortTextField;
    private JLabel labConnectionName;
    private JLabel labelConnectiondes;
    private JLabel lableMRHost;
    private JLabel lableHDFSHost;
    private JLabel labeUserName;
    private JLabel lablePassword;
    private JLabel labelMRPort;
    private JLabel labelHDFSPort;
    private GenericConnectionFileSystemSettings temporaryConfig;

    public GenericFileSystemSettingsForm(GenericConnectionFileSystemSettings connectionConfig)
    {
        super(connectionConfig);
        updateUiLanguage();
        temporaryConfig = connectionConfig.clone();
        updateBorderTitleForeground(connectionParametersPanel);
        resetFormChanges();
        registerComponent(mainPanel);
        userTextField.setEnabled(!osAuthenticationCheckBox.isSelected());
        passwordField.setEnabled(!osAuthenticationCheckBox.isSelected());
        infoButton.setVisible(false);
        osAuthenticationCheckBox.setVisible(false);
        passwordField.setVisible(false);
        lablePassword.setVisible(false);
    }


    protected DocumentListener createDocumentListener()
    {
        return new DocumentAdapter()
        {
            protected void textChanged(DocumentEvent e)
            {
                GenericConnectionFileSystemSettings configuration = getConfiguration();
                configuration.setModified(true);
                Document document = e.getDocument();
                if (document == nameTextField.getDocument())
                {
                    ConnectionBundleSettings connectionBundleSettings = configuration.getParent().getParent();
                    ConnectionBundleSettingsForm settingsEditorForm = connectionBundleSettings.getSettingsEditor();
                    if (settingsEditorForm != null)
                    {
                        JList connectionList = settingsEditorForm.getList();
                        connectionList.revalidate();
                        connectionList.repaint();
                        notifyPresentationChanges();
                    }
                }
            }
        };
    }

    public void notifyPresentationChanges()
    {
        GenericConnectionFileSystemSettings configuration = temporaryConfig;//getConfiguration();
        String name = nameTextField.getText();
        ConnectivityStatus connectivityStatus = configuration.getConnectivityStatus();
        Icon icon = configuration.getParent().isNew() ? Icons.CONNECTION_NEW :
                !activeCheckBox.isSelected() ? Icons.CONNECTION_DISABLED :
                        connectivityStatus == ConnectivityStatus.VALID ? Icons.CONNECTION_ACTIVE :
                                connectivityStatus == ConnectivityStatus.INVALID ? Icons.CONNECTION_INVALID : Icons.CONNECTION_INACTIVE;

        ConnectionPresentationChangeListener listener = EventUtil.notify(configuration.getProject(), ConnectionPresentationChangeListener.TOPIC);
        listener.presentationChanged(name, icon, null, getConfiguration().getConnectionId(), configuration.getFileSystemType());

    }


    /**
     * 创建一个动作监听
     *
     * @return
     */
    protected ActionListener createActionListener()
    {
        return new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                Object source = e.getSource();
                ConnectionFileSystemSettings configuration = getConfiguration();

                if (source == testButton || source == infoButton)
                {
                    temporaryConfig = new GenericConnectionFileSystemSettings(getConfiguration().getParent());
                    applyChanges(temporaryConfig);
                    if (source == testButton) ConnectionManager.testConfigConnection(temporaryConfig, true);
                } else if (source == osAuthenticationCheckBox)
                {
                    userTextField.setEnabled(!osAuthenticationCheckBox.isSelected());
                    passwordField.setEnabled(!osAuthenticationCheckBox.isSelected());
                    getConfiguration().setModified(true);
                } else
                {
                    getConfiguration().setModified(true);
                }

                if (source == activeCheckBox || source == nameTextField || source == testButton || source == infoButton)
                {
                    ConnectionBundleSettings connectionBundleSettings = configuration.getParent().getParent();
                    ConnectionBundleSettingsForm settingsEditor = connectionBundleSettings.getSettingsEditor();

                    if (settingsEditor != null)
                    {
                        JList connectionList = settingsEditor.getList();
                        connectionList.revalidate();
                        connectionList.repaint();
                        notifyPresentationChanges();
                    }
                }
            }
        };
    }


    public String getConnectionName()
    {
        return nameTextField.getText();
    }

    public boolean isConnectionActive()
    {
        return activeCheckBox.isSelected();
    }

    public ConnectivityStatus getConnectivityStatus()
    {
        return temporaryConfig.getConnectivityStatus();
    }

    public JPanel getComponent()
    {
        return mainPanel;
    }

    public void applyChanges(GenericConnectionFileSystemSettings connectionConfig)
    {
        connectionConfig.setActive(activeCheckBox.isSelected());
        connectionConfig.setName(nameTextField.getText());
        connectionConfig.setDescription(descriptionTextField.getText());
        connectionConfig.setMapReduceHost(mapReduceHostTextField.getText());
        connectionConfig.setMapReducePort(mapReducePortTextField.getText());
        connectionConfig.setHdfsHost(hdfsHostTextField.getText());
        connectionConfig.setHdfsPort(hdfsPortTextField.getText());
        connectionConfig.setUser(userTextField.getText());
        connectionConfig.setPassword(new String(passwordField.getPassword()));
        connectionConfig.setOsAuthentication(osAuthenticationCheckBox.isSelected());
        connectionConfig.setConnectivityStatus(temporaryConfig.getConnectivityStatus());
        connectionConfig.updateHashCode();
    }

    public void applyFormChanges() throws ConfigurationException
    {
        ConfigurationEditorUtil.validateStringInputValue(nameTextField, "Name", true);
        final GenericConnectionFileSystemSettings connectionConfig = getConfiguration();
        final boolean settingsChanged =
                !CommonUtil.safeEqual(connectionConfig.getMapReducelUrl(), mapReduceHostTextField.getText()) ||
                        !CommonUtil.safeEqual(connectionConfig.getHDFSUrl(), hdfsHostTextField.getText()) ||
                        !CommonUtil.safeEqual(connectionConfig.getUser(), userTextField.getText());
        applyChanges(connectionConfig);
        new SettingsChangeNotifier()
        {
            @Override
            public void notifyChanges()
            {
                if (settingsChanged)
                {
                    Project project = connectionConfig.getProject();
                    EventUtil.notify(project, ConnectionSettingsListener.TOPIC).connectionChanged(connectionConfig.getConnectionId());
                }
            }
        };
    }


    public void resetFormChanges()
    {
        GenericConnectionFileSystemSettings connectionConfig = getConfiguration();
        activeCheckBox.setSelected(connectionConfig.isActive());
        nameTextField.setText(connectionConfig.getDisplayName());
        descriptionTextField.setText(connectionConfig.getDescription());
        hdfsHostTextField.setText(connectionConfig.getHdfsHost());
        hdfsPortTextField.setText(connectionConfig.getHdfsPort());
        mapReduceHostTextField.setText(connectionConfig.getMapReduceHost());
        mapReducePortTextField.setText(connectionConfig.getMapReducePort());
        userTextField.setText(connectionConfig.getUser());
        passwordField.setText(connectionConfig.getPassword());
        osAuthenticationCheckBox.setSelected(connectionConfig.isOsAuthentication());
    }

    @Override
    public void dispose()
    {
        super.dispose();
    }

    public void updateUiLanguage()
    {
        ResourceBundle resourceBundle = LocaleLanguageManager.getInstance().getResourceBundle();
        if (resourceBundle == null) return;
        activeCheckBox.setText(resourceBundle.getString(LanguageKeyWord.SETTINGACTIVE));
        TitledBorder border = (TitledBorder) connectionParametersPanel.getBorder();
        border.setTitle(resourceBundle.getString(LanguageKeyWord.SETTINGHADOOPCONNECTION));
        labConnectionName.setText(resourceBundle.getString(LanguageKeyWord.SETTINGCONNECTIONNAME));
        labelConnectiondes.setText(resourceBundle.getString(LanguageKeyWord.SETTINGCONNECTIONDES));
        lableMRHost.setText(resourceBundle.getString(LanguageKeyWord.SETTINGMRURL));
        lableHDFSHost.setText(resourceBundle.getString(LanguageKeyWord.SETTINGHDFSURL));
        labeUserName.setText(resourceBundle.getString(LanguageKeyWord.SETTINGUSERNAME));
        lablePassword.setText(resourceBundle.getString(LanguageKeyWord.SETTINGUSERPASSWORD));
        labelMRPort.setText(resourceBundle.getString(LanguageKeyWord.SETTINGPROT));
        labelHDFSPort.setText(resourceBundle.getString(LanguageKeyWord.SETTINGPROT));
        osAuthenticationCheckBox.setText(resourceBundle.getString(LanguageKeyWord.SETTINGUSERAUTHENTICATION));
        testButton.setText(resourceBundle.getString(LanguageKeyWord.SETTINGTEST));
        infoButton.setText(resourceBundle.getString(LanguageKeyWord.SETTINGINFOR));
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
        mainPanel.setLayout(new GridLayoutManager(3, 2, new Insets(12, 4, 4, 4), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        testButton = new JButton();
        testButton.setText("测试");
        panel1.add(testButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        infoButton = new JButton();
        infoButton.setText("信息");
        panel1.add(infoButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        mainPanel.add(spacer2, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel2, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        connectionParametersPanel = new JPanel();
        connectionParametersPanel.setLayout(new GridLayoutManager(7, 5, new Insets(8, 8, 8, 8), -1, -1));
        panel2.add(connectionParametersPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        connectionParametersPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Hadoop连接参数"));
        labConnectionName = new JLabel();
        labConnectionName.setText("连接名称：");
        connectionParametersPanel.add(labConnectionName, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labelConnectiondes = new JLabel();
        labelConnectiondes.setText("描       述：");
        connectionParametersPanel.add(labelConnectiondes, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        nameTextField = new JTextField();
        nameTextField.setMargin(new Insets(1, 3, 1, 1));
        connectionParametersPanel.add(nameTextField, new GridConstraints(0, 1, 1, 4, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        descriptionTextField = new JTextField();
        descriptionTextField.setMargin(new Insets(1, 3, 1, 1));
        connectionParametersPanel.add(descriptionTextField, new GridConstraints(1, 1, 1, 4, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        lableMRHost = new JLabel();
        lableMRHost.setText("M/RV2地址：");
        connectionParametersPanel.add(lableMRHost, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lableHDFSHost = new JLabel();
        lableHDFSHost.setText("HDFS地址：");
        connectionParametersPanel.add(lableHDFSHost, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hdfsHostTextField = new JTextField();
        hdfsHostTextField.setMargin(new Insets(1, 3, 1, 1));
        connectionParametersPanel.add(hdfsHostTextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        labeUserName = new JLabel();
        labeUserName.setText("用户名称：");
        connectionParametersPanel.add(labeUserName, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        userTextField = new JTextField();
        userTextField.setMargin(new Insets(1, 3, 1, 1));
        connectionParametersPanel.add(userTextField, new GridConstraints(4, 1, 1, 4, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final Spacer spacer3 = new Spacer();
        connectionParametersPanel.add(spacer3, new GridConstraints(5, 3, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        mapReduceHostTextField = new JTextField();
        connectionParametersPanel.add(mapReduceHostTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        labelMRPort = new JLabel();
        labelMRPort.setText("端口：");
        connectionParametersPanel.add(labelMRPort, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        mapReducePortTextField = new JTextField();
        connectionParametersPanel.add(mapReducePortTextField, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        labelHDFSPort = new JLabel();
        labelHDFSPort.setText("端口：");
        connectionParametersPanel.add(labelHDFSPort, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        hdfsPortTextField = new JTextField();
        connectionParametersPanel.add(hdfsPortTextField, new GridConstraints(3, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        osAuthenticationCheckBox = new JCheckBox();
        osAuthenticationCheckBox.setText("使用系统用户认证");
        connectionParametersPanel.add(osAuthenticationCheckBox, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        lablePassword = new JLabel();
        lablePassword.setText("用户组：");
        connectionParametersPanel.add(lablePassword, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        passwordField = new JPasswordField();
        passwordField.setMargin(new Insets(1, 3, 1, 1));
        connectionParametersPanel.add(passwordField, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_NORTHWEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        activeCheckBox = new JCheckBox();
        activeCheckBox.setMargin(new Insets(0, 0, 0, 0));
        activeCheckBox.setText("激活");
        activeCheckBox.setMnemonic('激');
        activeCheckBox.setDisplayedMnemonicIndex(0);
        panel2.add(activeCheckBox, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        labConnectionName.setLabelFor(nameTextField);
        labelConnectiondes.setLabelFor(descriptionTextField);
        lableHDFSHost.setLabelFor(hdfsHostTextField);
        labeUserName.setLabelFor(userTextField);
        lablePassword.setLabelFor(passwordField);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}
