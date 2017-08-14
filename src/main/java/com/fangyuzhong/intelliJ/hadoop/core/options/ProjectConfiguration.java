package com.fangyuzhong.intelliJ.hadoop.core.options;

import com.fangyuzhong.intelliJ.hadoop.core.options.ui.ConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.util.ProjectSupplier;
import com.intellij.openapi.project.Project;

/**
 * 定义工程配置类
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class ProjectConfiguration<T extends ConfigurationEditorForm>
        extends Configuration<T>
        implements ProjectSupplier
{

    private Project project;

    /**
     * 初始化工程配置
     * @param project
     */
    public ProjectConfiguration(Project project)
    {
        this.project = project;
    }

    /**
     *获取工程Project
     * @return
     */
    public Project getProject()
    {
        return this.project;
    }
}