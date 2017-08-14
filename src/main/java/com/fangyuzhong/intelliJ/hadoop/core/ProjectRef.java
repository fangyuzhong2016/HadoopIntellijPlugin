package com.fangyuzhong.intelliJ.hadoop.core;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class ProjectRef
{
    private WeakReference<Project> ref;

    public ProjectRef(Project project)
    {
        this.ref = new WeakReference(project);
    }

    @Nullable
    public Project get()
    {
        return (Project) this.ref.get();
    }
}