package com.fangyuzhong.intelliJ.hadoop.fsconnection.config.action;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.FileSystemType;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionConfigType;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.ConnectionBundleSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-22.
 */
public class CreateConnectionAction
        extends ConnectionSettingsAction
{
    private FileSystemType fileSystemType;

    public CreateConnectionAction(@Nullable FileSystemType fileSystemType)
    {
        super(getName(fileSystemType), getIcon(fileSystemType));
        this.fileSystemType = fileSystemType;
    }

    private static Icon getIcon(@Nullable FileSystemType fileSystemType)
    {
        return fileSystemType == null ? null : fileSystemType.getIcon();
    }

    private static String getName(@Nullable FileSystemType fileSystemType)
    {
        return fileSystemType == null ? "Custom..." : fileSystemType.getDisplayName();
    }

    public void actionPerformed(AnActionEvent e)
    {
        ConnectionBundleSettingsForm settingsEditor = getSettingsForm(e);
        if (settingsEditor != null)
        {
            FileSystemType fileSystemType = this.fileSystemType;
            ConnectionConfigType configType = ConnectionConfigType.BASIC;
            if (fileSystemType == null)
            {
                configType = ConnectionConfigType.CUSTOM;
                fileSystemType = FileSystemType.UNKNOWN;
            }
           // settingsEditor.createNewConnection(fileSystemType, configType);
        }
    }
}
