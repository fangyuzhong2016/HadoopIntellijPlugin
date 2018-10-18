package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectivityStatus;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.FileSystemType;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.GenericFileSystemSettingsForm;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.util.Base64Converter;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

/**
 * 定义文件系统连接基本配置信息
 */
public abstract class ConnectionFileSystemSettings extends
        Configuration<GenericFileSystemSettingsForm>
{
    public static final Logger LOGGER = LoggerFactory.createLogger();

    private transient ConnectivityStatus connectivityStatus = ConnectivityStatus.UNKNOWN;
    protected boolean active = true;
    protected boolean osAuthentication = false;
    protected String name="";
    protected String description="";
    protected FileSystemType fileSystemType = FileSystemType.HDFS;
    protected double databaseVersion = 9999;
    protected String user="";
    protected String password="";
    protected int hashCode;
    private ConnectionSettings parent;

    /**
     *
     * @param parent
     */
    public ConnectionFileSystemSettings(ConnectionSettings parent)
    {
        this.parent = parent;
    }

    /**
     *
     * @return
     */
    public ConnectionSettings getParent()
    {
        return parent;
    }

    protected static String nvl(Object value)
    {
        return (String) (value == null ? "" : value);
    }

    public ConnectivityStatus getConnectivityStatus()
    {
        return connectivityStatus;
    }

    public void setConnectivityStatus(ConnectivityStatus connectivityStatus)
    {
        this.connectivityStatus = connectivityStatus;
    }

    public boolean isOsAuthentication()
    {
        return osAuthentication;
    }

    public void setOsAuthentication(boolean osAuthentication)
    {
        this.osAuthentication = osAuthentication;
        updateHashCode();
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    /**
     * 获取连接名称
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * 设置连接名称
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    public String getDisplayName()
    {
        return name;
    }


    /**
     * 获取描述设置
     * @return
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * 设置连接描述
     * @param description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * 获取连接类型
     * @return
     */
    public FileSystemType getFileSystemType()
    {
        return fileSystemType;
    }

    /**
     * 设置连接类型
     * @param fileSystemType
     */
    public void setFileSystemType(FileSystemType fileSystemType)
    {
        this.fileSystemType = fileSystemType;
    }

    /**
     * 获取连接版本
     * @return
     */
    public double getFileSystemVersion()
    {
        return databaseVersion;
    }

    /**
     * 设置连接版本
     * @param databaseVersion
     */
    public void setFileSystemVersion(double databaseVersion)
    {
        this.databaseVersion = databaseVersion;
    }

    /**
     *
     * @return
     */
    public String getUser()
    {
        return user;
    }

    /**
     *
     * @param user
     */
    public void setUser(String user)
    {
        this.user = user;
    }

    /**
     *
     * @return
     */
    public String getPassword()
    {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getConnectionDetails()
    {
        return "Name:\t" + name + "\n" +
                "Description:\t" + description + "\n" +
                "User:\t" + user;
    }

    @Override
    public String getConfigElementName()
    {
        return "database";
    }

    /**
     * 获取MapReduce的连接字符串
     * @return
     */
    public abstract String getMapReducelUrl();
    /**
     * 获取HDFS文件系统的连接字符串
     * @return
     */
    public abstract String getHDFSUrl();

    public abstract void updateHashCode();

    @Override
    public int hashCode()
    {
        return hashCode;
    }

    @NotNull
    public String getConnectionId()
    {
        return parent.getConnectionId();
    }

    /*********************************************************
     *                 PersistentConfiguration               *
     *********************************************************/
    /**
     * 读取配置并解析
     * @param element
     */
    public void readConfiguration(Element element)
    {
        String connectionId = getString(element, "id", null);
        if (connectionId != null)
        {
            parent.setConnectionId(connectionId);
        }
        name = getString(element, "name", name);
        description = getString(element, "description", description);
        fileSystemType = FileSystemType.get(getString(element, "database-type", fileSystemType.getName()));
        databaseVersion = getDouble(element, "database-version", databaseVersion);
        user = getString(element, "user", user);
        if(StringUtil.isEmptyOrSpaces(user))
        {
            user=System.getProperty("user.name");
        }
		System.setProperty("HADOOP_USER_NAME", user);
        password = decodePassword(getString(element, "password", password));
        active = getBoolean(element, "active", active);
        osAuthentication = getBoolean(element, "os-authentication", osAuthentication);
        updateHashCode();
    }

    /**
     * 将设置的配置项写入XML
     * @param element
     */
    public void writeConfiguration(Element element)
    {
        setString(element, "name", nvl(name));
        setString(element, "description", nvl(description));
        setBoolean(element, "active", active);
        setBoolean(element, "os-authentication", osAuthentication);
        setString(element, "database-type", nvl(fileSystemType == null ? FileSystemType.UNKNOWN.getName() : fileSystemType.getName()));
        setDouble(element, "database-version", databaseVersion);
        setString(element, "user", nvl(user));
        setString(element, "password", encodePassword(password));
    }

    private static String encodePassword(String password)
    {
        try
        {
            password = StringUtil.isEmpty(password) ? "" : Base64Converter.encode(nvl(password));
        } catch (Exception e)
        {
            // any exception would break the logic storing the fsconnection settings
            LOGGER.error("Error encoding password", e);
        }
        return password;
    }

    private static String decodePassword(String password)
    {
        try
        {
            password = StringUtil.isEmpty(password) ? "" : Base64Converter.decode(nvl(password));
        } catch (Exception e)
        {
            // password may not be encoded yet
        }

        return password;
    }

    public Project getProject()
    {
        return parent.getProject();
    }
}
