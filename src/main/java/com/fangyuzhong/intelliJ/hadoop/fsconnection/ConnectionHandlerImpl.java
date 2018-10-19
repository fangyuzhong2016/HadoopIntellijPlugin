package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.Constants;
import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettings;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundleImpl;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.io.IOException;

/**
 * 定义HDFS连接接口ConnectionHandler的实现类
 * Created by fangyuzhong on 17-7-16.
 */
public class ConnectionHandlerImpl  extends DisposableBase
        implements ConnectionHandler
{
    private FileSystem fileSystem=null;
    private Configuration configurationHdfs=null;
    private ConnectionBundle connectionBundle;
    private ConnectionStatus connectionStatus;
    private ConnectionHandlerRef ref;
    private ConnectionInfo connectionInfo;
    private ConnectionSettings connectionSettings;
    private FileSystemObjectBundle objectBundle=null;
    private boolean canConnection=false;
    public static final Logger LOGGER = LoggerFactory.createLogger();
    /**
     * 初始化连接实现
     * @param connectionBundle
     * @param connectionSettings
     */
    public ConnectionHandlerImpl(ConnectionBundle connectionBundle, ConnectionSettings connectionSettings)
    {
        this.connectionBundle = connectionBundle;
        this.connectionSettings = connectionSettings;
        configurationHdfs = new Configuration(false);
        ClassLoader pClassLoader=null;
        try
        {
            pClassLoader= Class.forName(com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager.class.getName()).getClassLoader();
        }
        catch (Exception ex)
        {
            LOGGER.error("获取当前类的加载器错误",ex);
        }
        if(pClassLoader!=null)
        {
            String hdfsPath = connectionSettings.getFileSystemSettings().getHDFSUrl();
            String yarnResourceAMPath = connectionSettings.getFileSystemSettings().getMapReducelUrl();
            if(!StringUtil.isEmptyOrSpaces(hdfsPath))
            {
                //设置HDFS的配置类加载器
                configurationHdfs.setClassLoader(pClassLoader);
                //设置HDFS相关属性
                configurationHdfs.set(Constants.FS_HDFS_IMPL_KEY, Constants.FS_HDFS_IMPL_VALUE);
                configurationHdfs.set(Constants.FS_FILE_IMPL_KEY,Constants.FS_FILE_IMPL_VALUE);
                configurationHdfs.set(Constants.FS_DEFAULTFS_KEY, hdfsPath);
                configurationHdfs.set(Constants.YARN_RESOURCEMANAGER_SCHEDULER_ADDRESS,yarnResourceAMPath);
				 /*************解决卡死的问题*******************************************************/
                configurationHdfs.set(Constants.IPC_CLIENT_CONNECT_TIMEOUT, "3000"); //默认连接3s超时
                configurationHdfs.set(Constants.IPC_CLIENT_CONNECT_MAX_RETRIES_ON_TIMEOUTS, "1");// 超时后重试1次
                /*********************************************************************************/
                //通过配置初始化HDFS的FileSystem
                InitiFileSystem(configurationHdfs);
            }
        }
        try
        {
            canConnection = createTestConnection();
        }
        catch (IOException ex)
        {
            canConnection=false;
            LOGGER.error("创建连接测试异常",ex);
        }
        connectionInfo = new ConnectionInfo(connectionSettings);
        connectionStatus = new ConnectionStatus();
        ref = new ConnectionHandlerRef(this);
        connectionStatus.setConnected(canConnection);
    }

    private  void  InitiFileSystem(Configuration configuration)
    {
        try
        {
            fileSystem = FileSystem.get(configuration);
        }
        catch (Exception ex)
        {
            LOGGER.error("通过configuration获取HDFS系统对象异常",ex);
        }
    }

    /**
     * 设置连接配置
     * @param connectionSettings
     */
    public void setConnectionConfig(final ConnectionSettings connectionSettings)
    {
        this.connectionSettings = connectionSettings;
        String hdfsPath = connectionSettings.getFileSystemSettings().getHDFSUrl();
        if(configurationHdfs!=null)
        {
            configurationHdfs.set(Constants.FS_DEFAULTFS_KEY, hdfsPath);
            InitiFileSystem(configurationHdfs);
            try
            {
                canConnection = createTestConnection();
            }
            catch (IOException ex)
            {
                canConnection=false;
            }
            connectionInfo.setConnectionInfo(connectionSettings);
            connectionStatus.setConnected(canConnection);
        }
    }

    public ConnectionSettings getSettings()
    {
        return connectionSettings;
    }
    @Nullable
    @Override
    public ConnectionHandler getConnectionHandler()
    {
        return this;
    }

    @NotNull
    @Override
    public String getName()
    {
        return this.connectionSettings.getFileSystemSettings().getName();
    }

    @Nullable
    @Override
    public String getDescription()
    {
        String strDesc = this.connectionSettings.getFileSystemSettings().getHDFSUrl();
        if(StringUtil.isEmptyOrSpaces(strDesc))
        {
            strDesc = getName();
        }
        return strDesc;
    }

    @Nullable
    @Override
    public Icon getIcon()
    {
        if(connectionStatus.isConnected())
        {
            return Icons.FILE_SYSTEM_HDFS;
        }
        else
        {
            return Icons.HDFS_CANNOTCONNECTION;
        }
    }

    @Override
    public Project getProject()
    {
        return getConnectionBundle().getProject();
    }

    @Override
    public boolean createTestConnection() throws IOException
    {
        boolean canConnection = false;
        if (fileSystem == null)
        {
            canConnection = false;
        }
        else
        {
			fileSystem.getConf().set(Constants.IPC_CLIENT_CONNECT_TIMEOUT, "3000"); //默认连接3s超时
            fileSystem.getConf().set(Constants.IPC_CLIENT_CONNECT_MAX_RETRIES_ON_TIMEOUTS, "1");// 超时后重试1次
            canConnection = fileSystem.exists(new Path("/"));
        }
        return canConnection;
    }

    @Override
    public Configuration getMainConnection()
    {
        return configurationHdfs;
    }

    @Override
    public FileSystem getMainFileSystem()
    {
        return fileSystem;
    }

    @Override
    public ConnectionStatus getConnectionStatus()
    {
        return this.connectionStatus;
    }

    @Override
    public ConnectionInfo getConnectionInfo()
    {
        return connectionInfo;
    }

    @Override
    public void setConnectionInfo(ConnectionInfo paramConnectionInfo)
    {

    }

    @Override
    public boolean isActive()
    {
        return this.connectionSettings.getFileSystemSettings().isActive();
    }

    @Override
    public FileSystemType getFileSystemType()
    {
        return this.connectionSettings.getFileSystemSettings().getFileSystemType();
    }

    @Override
    public double getFileSystemVersion()
    {
        return this.connectionSettings.getFileSystemSettings().getFileSystemVersion();
    }

    @Override
    public boolean isConnected()
    {
        return connectionStatus.isConnected();
    }

    @Override
    public int getIdleMinutes()
    {
        return 0;
    }

    @Override
    public FileSystemInfo getFileSystemInfo()
    {
        return null;
    }

    @Override
    public void disconnect()
    {
        connectionStatus.setConnected(false);
        getObjectBundle().refreshTreeChildren();
    }

    @Override
    public String getId()
    {
        return connectionSettings.getConnectionId();
    }

    @Override
    public String getUserName()
    {
        return null;
    }

    @Override
    public String getPresentableText()
    {
        return this.getDescription();
    }

    @Override
    public ConnectionHandlerRef getRef()
    {
        return ref;
    }

    @Override
    public boolean isVirtual()
    {
        return false;
    }

    @Override
    public ConnectionLoadMonitor getLoadMonitor()
    {
        return null;
    }

    @Override
    public ConnectionBundle getConnectionBundle()
    {
        return FailsafeUtil.get(this.connectionBundle);
    }

    @Override
    public boolean canConnect()
    {
     return canConnection;
    }

    @Override
    public boolean isValid(boolean paramBoolean)
    {
        return true;
    }

    @Override
    public boolean isValid()
    {
        return true;
    }

    /**
     *获取连接对应的HDFS根目录下所有的对象集合
     * @return
     */
    @Override
    public FileSystemObjectBundle getObjectBundle()
    {
        if (objectBundle == null)
        {
            objectBundle = new FileSystemObjectBundleImpl(this, connectionBundle);
        }
        return objectBundle;
    }

    @Override
    public Filter<FileSystemBrowserTreeNode> getObjectTypeFilter()
    {
        return null;
    }
}
