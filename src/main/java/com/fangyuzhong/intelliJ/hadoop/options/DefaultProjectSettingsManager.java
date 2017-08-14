package com.fangyuzhong.intelliJ.hadoop.options;


import com.fangyuzhong.intelliJ.hadoop.core.EventManager;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.impl.ProjectLifecycleListener;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 *
 */
@State(name = "HadoopNavigator.DefaultProject.Settings",
        storages = {@com.intellij.openapi.components.Storage(file = "$APP_CONFIG$/hdfsnavigator.xml")})
public class DefaultProjectSettingsManager implements ApplicationComponent,
        PersistentStateComponent<Element>
{
    private ProjectSettings defaultProjectSettings;

    private DefaultProjectSettingsManager()
    {
        defaultProjectSettings = new ProjectSettings(ProjectManager.getInstance().getDefaultProject());
    }

    public static DefaultProjectSettingsManager getInstance()
    {
        return ApplicationManager.getApplication().getComponent(DefaultProjectSettingsManager.class);
    }

    public ProjectSettings getDefaultProjectSettings()
    {
        return defaultProjectSettings;
    }

    @Override
    public void initComponent()
    {
        EventUtil.subscribe(null, ProjectLifecycleListener.TOPIC, this.projectLifecycleListener);
    }

    @Override
    public void disposeComponent()
    {
        EventManager.unsubscribe(projectLifecycleListener);
    }

    @NotNull
    @Override
    public String getComponentName()
    {
        return "HadoopNavigator.Application.TemplateProjectSettings";
    }

    /****************************************
     *       PersistentStateComponent       *
     *****************************************/
    @Nullable
    @Override
    public Element getState()
    {
        Element element = new Element("state");
        defaultProjectSettings.writeConfiguration(element);
        return element;
    }

    @Override
    public void loadState(Element element)
    {
        defaultProjectSettings.readConfiguration(element);
    }

    /*********************************************************
     *              ProjectLifecycleListener                 *
     *********************************************************/
    private ProjectLifecycleListener projectLifecycleListener = new ProjectLifecycleListener.Adapter()
    {

        @Override
        public void projectComponentsInitialized(final Project project)
        {

        }
    };
}
