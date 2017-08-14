package com.fangyuzhong.intelliJ.hadoop.core.ui.dialog;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableProjectComponent;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposerUtil;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemForm;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class FileSystemDialog<C extends FileSystemForm>
        extends DialogWrapper
        implements DisposableProjectComponent
{
    protected C component;
    private Project project;
    private boolean disposed;
    private boolean rememberSelection;

    protected FileSystemDialog(Project project, String title, boolean canBeParent)
    {
        super(project, canBeParent);
        setTitle(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SYSTEMTEXT)+"-" + title);
        this.project = project;
    }

    public final C getComponent()
    {
        return this.component;
    }

    @Nullable
    protected final JComponent createCenterPanel()
    {
        if (this.component == null)
        {
            throw new IllegalStateException("Component not created");
        }
        return this.component.getComponent();
    }

    protected String getDimensionServiceKey()
    {
        return "HadoopNavigator." + getClass().getSimpleName();
    }

    public JComponent getPreferredFocusedComponent()
    {
        JComponent focusComponent = this.component == null ? null :
                this.component.getPreferredFocusedComponent();
        return focusComponent == null ? super.getPreferredFocusedComponent() : focusComponent;
    }

    @NotNull
    public Project getProject()
    {
       return this.project;
    }

    public boolean isRememberSelection()
    {
        return this.rememberSelection;
    }

    public void registerRememberSelectionCheckBox(final JCheckBox rememberSelectionCheckBox)
    {
        rememberSelectionCheckBox.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                FileSystemDialog.this.rememberSelection = rememberSelectionCheckBox.isSelected();
            }
        });
    }

    public void dispose()
    {
        if (!this.disposed)
        {
            this.disposed = true;
            DisposerUtil.dispose(this.component);
            this.component = null;
            this.project = null;
            super.dispose();
        }
    }

    public boolean isDisposed()
    {
        return this.disposed;
    }
}
