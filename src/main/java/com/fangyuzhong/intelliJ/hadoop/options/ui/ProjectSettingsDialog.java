package com.fangyuzhong.intelliJ.hadoop.options.ui;


import com.fangyuzhong.intelliJ.hadoop.core.ui.dialog.FileSystemDialog;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.options.ConfigId;
import com.fangyuzhong.intelliJ.hadoop.options.ProjectSettings;
import com.fangyuzhong.intelliJ.hadoop.options.ProjectSettingsManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.help.HelpManager;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ProjectSettingsDialog extends FileSystemDialog<ProjectSettingsEditorForm>
{
    private JButton bApply;
    private ProjectSettings projectSettings;

    public ProjectSettingsDialog(Project project)
    {
        super(project, LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGTEXT), true);
        setModal(true);
        setResizable(true);
        projectSettings = getProjectSettings();
        projectSettings.createCustomComponent();
        component = projectSettings.getSettingsEditor();
        if (component != null) component.setDialog(this);

        init();
        setCancelButtonText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGCANCEL));
        setOKButtonText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGOK));
    }

    private ProjectSettings getProjectSettings()
    {
        return ProjectSettingsManager.getSettings(getProject());
    }

    public void dispose()
    {
        super.dispose();
    }

    @NotNull
    protected final Action[] createActions()
    {
        return new Action[]{
                getOKAction(),
                getCancelAction(),
                new ApplyAction(),
                getHelpAction()
        };
    }

    protected JButton createJButtonForAction(Action action)
    {
        if (action instanceof ApplyAction)
        {
            bApply = new JButton();
            bApply.setAction(action);
            bApply.setEnabled(false);
            return bApply;
        }
        return super.createJButtonForAction(action);
    }

    public void doCancelAction()
    {
        //projectSettings.reset();
        projectSettings.disposeUIResources();
        super.doCancelAction();
    }

    public void doOKAction()
    {
        try
        {
            projectSettings.apply();
            projectSettings.disposeUIResources();
            super.doOKAction();
        } catch (ConfigurationException e)
        {
            MessageUtil.showErrorDialog(getProject(), e.getMessage());
        }

    }

    public void doApplyAction()
    {
        try
        {
            projectSettings.apply();
            bApply.setEnabled(false);
            setCancelButtonText("Close");
        } catch (ConfigurationException e)
        {
            MessageUtil.showErrorDialog(getProject(), e.getTitle(), e.getMessage());
        }
    }

    protected void doHelpAction()
    {
        HelpManager.getInstance().invokeHelp(projectSettings.getHelpTopic());
    }

    private class ApplyAction extends AbstractAction
    {
        private Alarm alarm = new Alarm(Alarm.ThreadToUse.SWING_THREAD);
        private Runnable reloader = new Runnable()
        {
            public void run()
            {
                if (isShowing())
                {
                    boolean isModified = projectSettings.isModified();
                    bApply.setEnabled(isModified);
                    //setCancelButtonText(isModified ?  : "Close");
                    addReloadRequest();
                }
            }
        };

        private void addReloadRequest()
        {
            alarm.addRequest(reloader, 500, ModalityState.stateForComponent(getWindow()));
        }

        public ApplyAction()
        {
            putValue(Action.NAME, LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGAPPLY));
            putValue(DEFAULT_ACTION, Boolean.FALSE);
            addReloadRequest();
        }

        public void actionPerformed(ActionEvent e)
        {
            doApplyAction();
        }
    }

    public void focusConnectionSettings(@Nullable ConnectionHandler connectionHandler)
    {
        ProjectSettingsEditorForm globalSettingsEditor = projectSettings.getSettingsEditor();
        if (globalSettingsEditor != null)
        {
            globalSettingsEditor.focusConnectionSettings(connectionHandler);
        }
    }

    public void focusSettings(ConfigId configId)
    {
        ProjectSettingsEditorForm globalSettingsEditor = projectSettings.getSettingsEditor();
        if (globalSettingsEditor != null)
        {
            globalSettingsEditor.focusSettingsEditor(configId);
        }
    }
}
