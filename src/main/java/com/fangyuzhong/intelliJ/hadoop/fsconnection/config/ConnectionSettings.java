package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.fangyuzhong.intelliJ.hadoop.core.options.CompositeProjectConfiguration;
import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.ConnectionSettingsForm;
import org.jdom.Element;

import java.util.UUID;

/**
 * 定义HDFS连接的配置
 * Created by fangyuzhong on 17-7-21.
 */
public class ConnectionSettings extends
        CompositeProjectConfiguration<ConnectionSettingsForm>
{
    private ConnectionBundleSettings parent;
    private String connectionId;
    private boolean isNew;
    private ConnectionFileSystemSettings fileSystemSettings;

    /**
     * 初始化
     * @param parent
     */
    public ConnectionSettings(ConnectionBundleSettings parent)
    {
        super(parent.getProject());
        this.parent = parent;
        fileSystemSettings = new GenericConnectionFileSystemSettings(this);

    }
    public ConnectionBundleSettings getParent()
    {
        return parent;
    }
    public ConnectionFileSystemSettings getFileSystemSettings()
    {
        return fileSystemSettings;
    }
    @Override
    protected Configuration[] createConfigurations()
    {
        return new Configuration[]{fileSystemSettings};
    }
    public void generateNewId()
    {
        connectionId = UUID.randomUUID().toString();
    }
    public void setConnectionId(String connectionId)
    {
        this.connectionId = connectionId;
    }
    @Override
    protected ConnectionSettingsForm createConfigurationEditor()
    {
        return new ConnectionSettingsForm(this);
    }
    public String getConnectionId()
    {
        return connectionId;
    }
    @Override
    public void readConfiguration(Element element)
    {
        if (ConnectionBundleSettings.IS_IMPORT_EXPORT_ACTION.get())
        {
            generateNewId();
        } else
        {
            connectionId = element.getAttributeValue("id");
        }
        super.readConfiguration(element);
    }

    public boolean isNew()
    {
        return isNew;
    }

    public void setNew(boolean isNew)
    {
        this.isNew = isNew;
    }

    @Override
    public void writeConfiguration(Element element)
    {
        element.setAttribute("id", connectionId);
        super.writeConfiguration(element);
    }

    public ConnectionSettings clone()
    {
        try
        {
            Element connectionElement = new Element("Connection");
            writeConfiguration(connectionElement);
            ConnectionSettings clone = new ConnectionSettings(parent);
            clone.readConfiguration(connectionElement);
            clone.fileSystemSettings.setConnectivityStatus(fileSystemSettings.getConnectivityStatus());
            clone.generateNewId();
            return clone;
        } catch (Exception e)
        {
            return null;
        }

    }
}
