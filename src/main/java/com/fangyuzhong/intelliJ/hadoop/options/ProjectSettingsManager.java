package com.fangyuzhong.intelliJ.hadoop.options;


import com.fangyuzhong.intelliJ.hadoop.core.action.FileSystemDataKeys;
import com.fangyuzhong.intelliJ.hadoop.core.message.MessageCallback;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionBundleSettings;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettingsListener;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.options.ui.ProjectSettingsDialog;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 定义插件的配置初始化入口
 */
@State(name = "HadoopNavigator.Project.Settings",
        storages = {@com.intellij.openapi.components.Storage(file = "$PROJECT_CONFIG_DIR$/hdfsnavigator.xml",
                scheme = com.intellij.openapi.components.StorageScheme.DIRECTORY_BASED),
                @com.intellij.openapi.components.Storage(file = "$PROJECT_FILE$")})
public class ProjectSettingsManager implements ProjectComponent, PersistentStateComponent<Element>
{
    private ProjectSettings projectSettings;

    private ProjectSettingsManager(Project project)
    {
        projectSettings = new ProjectSettings(project);
    }

    public static ProjectSettingsManager getInstance(Project project)
    {
        return project.getComponent(ProjectSettingsManager.class);
    }

    public static ProjectSettings getSettings(Project project)
    {
        if (project.isDefault())
        {
            return DefaultProjectSettingsManager.getInstance().getDefaultProjectSettings();
        } else
        {
            return getInstance(project).projectSettings;
        }
    }

    public ProjectSettings getProjectSettings()
    {
        return projectSettings;
    }


    public ConnectionBundleSettings getConnectionSettings()
    {
        return projectSettings.getConnectionSettings();
    }



    public void openDefaultProjectSettings()
    {
        Project project = ProjectManager.getInstance().getDefaultProject();
        ProjectSettingsDialog globalSettingsDialog = new ProjectSettingsDialog(project);
        globalSettingsDialog.show();
    }

    public void openProjectSettings(ConfigId configId)
    {
        Project project = getProject();
        ProjectSettingsDialog globalSettingsDialog = new ProjectSettingsDialog(project);
        globalSettingsDialog.focusSettings(configId);
        globalSettingsDialog.show();
    }

    public void openConnectionSettings(@Nullable ConnectionHandler connectionHandler)
    {
        Project project = getProject();
        ProjectSettingsDialog globalSettingsDialog = new ProjectSettingsDialog(project);
        globalSettingsDialog.focusConnectionSettings(connectionHandler);
        globalSettingsDialog.show();
    }

    private Project getProject()
    {
        return projectSettings.getProject();
    }

    @Override
    public void projectOpened()
    {
    }

    @Override
    public void projectClosed()
    {
    }

    /**
     * 初始化工程，判断当前工程中是否有该插件配置文件，如果没有，将会生成新的配置
     */
    @Override
    public void initComponent()
    {
        importDefaultSettings(true);
    }


    /**
     * 导入新的配置
     * @param isNewProject
     */
    public void importDefaultSettings(final boolean isNewProject)
    {
        final Project project = getProject();
        Boolean settingsLoaded = project.getUserData(FileSystemDataKeys.PROJECT_SETTINGS_LOADED_KEY);
        if ((settingsLoaded == null) || (!settingsLoaded.booleanValue()) || (!isNewProject))
        {
            String message = LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.IMPORTDEFAULTSETTINGSASKINFORMATION) +
                    project.getName() + "\"?";
            String strYes=LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.ASKYES);
            String strNo=LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.ASKNO);
            MessageUtil.showQuestionDialog(project, LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.IMPORTDEFAULTSETTINGSASKTITLE),
                    message, new String[]{strYes, strNo}, 0, new MessageCallback(Integer.valueOf(0))
            {
                protected void execute()
                {

                    try
                    {
                        Element element = new Element("state");
                        ProjectSettings defaultProjectSettings = DefaultProjectSettingsManager.getInstance().
                                getDefaultProjectSettings();
                        defaultProjectSettings.writeConfiguration(element);
                        ConnectionBundleSettings.IS_IMPORT_EXPORT_ACTION.set(true);
                        getProjectSettings().readConfiguration(element);

                        (EventUtil.notify(project, ConnectionSettingsListener.TOPIC)).connectionsChanged();
                        if (!isNewProject)
                        {
                            MessageUtil.showInfoDialog(project, "Project Settings",
                                    "Default project settings loaded to project \"" + project.getName() + "\".");
                        }
                    } finally
                    {
                        ConnectionBundleSettings.IS_IMPORT_EXPORT_ACTION.set(false);
                    }

                }
            });
        }
    }

    @Override
    public void disposeComponent()
    {
    }

    @NotNull
    @Override
    public String getComponentName()
    {
        return "HadoopNavigator.Project.Settings";
    }

    /****************************************
     *       PersistentStateComponent       *
     *****************************************/
    @Nullable
    @Override
    public Element getState()
    {
        Element element = new Element("state");
        projectSettings.writeConfiguration(element);
        return element;
    }

    /**
     * 读取并解析配置
     * @param element
     */
    @Override
    public void loadState(Element element)
    {
        projectSettings.readConfiguration(element);
        getProject().putUserData(FileSystemDataKeys.PROJECT_SETTINGS_LOADED_KEY, true);
    }
}
