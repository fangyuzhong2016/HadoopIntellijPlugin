package com.fangyuzhong.intelliJ.hadoop.core;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.intellij.openapi.application.ApplicationAdapter;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import org.jetbrains.annotations.NotNull;

/**
 * 定义抽象Project的组件
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class AbstractProjectComponent
        extends ApplicationAdapter
        implements ProjectComponent, ProjectManagerListener, Disposable
{
    private Project project;

    protected AbstractProjectComponent(Project project)
    {
        this.project = project;
        ProjectManager projectManager = ProjectManager.getInstance();
        projectManager.addProjectManagerListener(project, this);
        ApplicationManager.getApplication().addApplicationListener(this);
    }

    @NotNull
    public Project getProject()
    {
        Project tmp7_4 = FailsafeUtil.get(this.project);
        if (tmp7_4 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/AbstractProjectComponent", "getProject"}));
        }
        return tmp7_4;
    }

    public void projectOpened()
    {
    }

    public void projectClosed()
    {
    }

    public void initComponent()
    {
    }

    public void projectOpened(Project project)
    {
    }

    public boolean canCloseProject(Project project)
    {
        return true;
    }

    private boolean disposed = false;

    public void projectClosed(Project project)
    {
    }

    public void projectClosing(Project project)
    {
    }

    public boolean isDisposed()
    {
        return this.disposed;
    }

    public void dispose()
    {
        this.disposed = true;
        this.project = null;
    }

    public final void disposeComponent()
    {
    }
}