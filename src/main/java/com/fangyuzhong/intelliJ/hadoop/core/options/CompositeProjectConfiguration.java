package com.fangyuzhong.intelliJ.hadoop.core.options;

import com.fangyuzhong.intelliJ.hadoop.core.options.ui.CompositeConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.util.ProjectSupplier;
import com.intellij.openapi.project.Project;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class CompositeProjectConfiguration<T extends CompositeConfigurationEditorForm>
        extends CompositeConfiguration<T>
        implements ProjectSupplier
{
    private Project project;

    public CompositeProjectConfiguration(Project project)
    {
        this.project = project;
    }

    public Project getProject()
    {
        return this.project;
    }
}
