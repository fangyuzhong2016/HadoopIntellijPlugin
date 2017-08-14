package com.fangyuzhong.intelliJ.hadoop.fsconnection.config.action;

import com.fangyuzhong.intelliJ.hadoop.core.action.FileSystemDataKeys;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.ConnectionBundleSettingsForm;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-22.
 */
public abstract class ConnectionSettingsAction
        extends DumbAwareAction
{
    public ConnectionSettingsAction(String text, Icon icon)
    {
        super(text, null, icon);
    }

    @Nullable
    ConnectionBundleSettingsForm getSettingsForm(AnActionEvent e)
    {
        return (ConnectionBundleSettingsForm) e.getData(FileSystemDataKeys.CONNECTION_BUNDLE_SETTINGS);
    }
}
