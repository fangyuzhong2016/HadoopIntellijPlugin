package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectivityStatus;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.GenericFileSystemSettingsForm;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class ConnectionConfigListCellRenderer extends DefaultListCellRenderer
{
    public Component getListCellRendererComponent(JList list, Object value,
                                                  int index, boolean isSelected,
                                                  boolean cellHasFocus)
    {
        ConnectionSettings connectionSettings = (ConnectionSettings) value;
        ConnectionFileSystemSettings databaseSettings = connectionSettings.getFileSystemSettings();
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        GenericFileSystemSettingsForm settingsEditor = databaseSettings.getSettingsEditor();
        String name = settingsEditor == null ?
                databaseSettings.getName() :
                settingsEditor.getConnectionName();

        ConnectivityStatus connectivityStatus = settingsEditor == null ?
                databaseSettings.getConnectivityStatus() :
                settingsEditor.getConnectivityStatus();

        boolean isActive = settingsEditor == null ?
                databaseSettings.isActive() :
                settingsEditor.isConnectionActive();

        Icon icon = Icons.FILE_SYSTEM_HDFS;
        boolean isNew = connectionSettings.isNew();

        if (isNew)
        {
            icon = connectivityStatus == ConnectivityStatus.VALID ? Icons.FILE_SYSTEM_HDFS : Icons.FILE_SYSTEM_HDFS;
        } else if (isActive)
        {
            icon = connectivityStatus == ConnectivityStatus.VALID ? Icons.FILE_SYSTEM_HDFS :
                    connectivityStatus == ConnectivityStatus.INVALID ? Icons.FILE_SYSTEM_HDFS : Icons.FILE_SYSTEM_HDFS;
        }

        label.setIcon(icon);
        label.setText(name);
        return label;
    }
}
