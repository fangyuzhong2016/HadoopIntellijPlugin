package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.core.options.ProjectConfiguration;
import com.fangyuzhong.intelliJ.hadoop.core.util.ThreadLocalFlag;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandlerImpl;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.ConnectionBundleSettingsForm;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.options.ConfigId;
import com.fangyuzhong.intelliJ.hadoop.options.ProjectSettingsManager;
import com.fangyuzhong.intelliJ.hadoop.options.TopLevelConfig;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class ConnectionBundleSettings extends
        ProjectConfiguration<ConnectionBundleSettingsForm>
        implements TopLevelConfig
{
    public static ThreadLocalFlag IS_IMPORT_EXPORT_ACTION = new ThreadLocalFlag(false);

    private ConnectionBundle connectionBundle;

    public ConnectionBundleSettings(Project project)
    {
        super(project);
        connectionBundle = new ConnectionBundle(project);
    }

    public static ConnectionBundleSettings getInstance(Project project)
    {
        return ProjectSettingsManager.getSettings(project).getConnectionSettings();
    }

    @NotNull
    @Override
    public String getId()
    {
        return "HadoopNavigator.Project.ConnectionSettings";
    }

    public String getDisplayName()
    {
        return LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGCONNECTIONINFORMATICA);
    }

    public String getHelpTopic()
    {
        return "connectionBundle";
    }

    @Override
    public ConfigId getConfigId()
    {
        return ConfigId.CONNECTIONS;
    }

    @Override
    public String getConfigElementName()
    {
        return "connections";
    }

    public boolean isModified()
    {
        if (super.isModified())
        {
            return true;
        }
        for (ConnectionHandler connectionHandler : connectionBundle.getConnectionHandlers())
        {
            if (connectionHandler.getSettings().isModified() || connectionHandler.getSettings().isNew()) return true;
        }
        return false;
    }


    public Configuration<ConnectionBundleSettingsForm> getOriginalSettings()
    {
        return getInstance(getProject());
    }

    /*********************************************************
     *                   UnnamedConfigurable                 *
     *********************************************************/
    public ConnectionBundleSettingsForm createConfigurationEditor()
    {
        return new ConnectionBundleSettingsForm(this);
    }

    /*********************************************************
     *                      Configurable                     *
     *********************************************************/
    /**
     * 读取配置并解析
     * @param element
     */
    public void readConfiguration(Element element)
    {
        if (IS_IMPORT_EXPORT_ACTION.get())
        {
            Project project = getProject();
            List<ConnectionHandler> connectionHandlers = connectionBundle.getAllConnectionHandlers();
            ConnectionManager.getInstance(project).disposeConnections(connectionHandlers);
        }

        for (Object o : element.getChildren())
        {
            Element connectionElement = (Element) o;
            String connectionId = connectionElement.getAttributeValue("id");
            ConnectionHandler connectionHandler = null;
            if (connectionId != null)
            {
                connectionHandler = connectionBundle.getConnection(connectionId);
            }

            if (connectionHandler == null)
            {
                //创建一个ConnectionHandler
                ConnectionSettings connectionSettings = new ConnectionSettings(this);
                connectionSettings.readConfiguration(connectionElement);
                connectionHandler = new ConnectionHandlerImpl(connectionBundle, connectionSettings);
                connectionBundle.addConnection(connectionHandler);
            } else
            {
                ConnectionSettings connectionSettings = connectionHandler.getSettings();
                connectionSettings.readConfiguration(connectionElement);
            }

        }
    }

    /**
     * 写入配置XML
     * @param element
     */
    public void writeConfiguration(Element element)
    {
        for (ConnectionHandler connectionHandler : connectionBundle.getConnectionHandlers().getFullList())
        {
            Element connectionElement = new Element("fsconnection");
            ConnectionSettings connectionSettings = connectionHandler.getSettings();
            connectionSettings.writeConfiguration(connectionElement);
            element.addContent(connectionElement);
        }
    }

    public ConnectionBundle getConnectionBundle()
    {
        return connectionBundle;
    }
}

