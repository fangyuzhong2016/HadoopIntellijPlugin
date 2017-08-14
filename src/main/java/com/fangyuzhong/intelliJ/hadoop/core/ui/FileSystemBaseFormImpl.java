package com.fangyuzhong.intelliJ.hadoop.core.ui;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableProjectComponent;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class FileSystemBaseFormImpl<P extends DisposableProjectComponent>
        extends DisposableBase
        implements FileSystemForm
{
    private boolean disposed;
    private Project project;
    private P parentComponent;

    public FileSystemBaseFormImpl()
    {
    }

    public FileSystemBaseFormImpl(@NotNull P parentComponent)
    {
        Disposer.register(parentComponent, this);
        this.parentComponent = parentComponent;
    }

    public FileSystemBaseFormImpl(Project project)
    {
        this.project = project;
    }


    public P getParentComponent()
    {
        return this.parentComponent;
    }

    @NotNull
    public final Project getProject()
    {
        if (this.project != null)
        {
            return this.project;
        }
        if (this.parentComponent != null)
        {
            return this.parentComponent.getProject();
        }
        DataContext dataContext = DataManager.getInstance().getDataContext(getComponent());
        Project project = (Project) PlatformDataKeys.PROJECT.getData(dataContext);
        return FailsafeUtil.get(project);
    }

    @Nullable
    public JComponent getPreferredFocusedComponent()
    {
        return null;
    }

    public void dispose()
    {
        super.dispose();
        this.project = null;
        this.parentComponent = null;
    }
}