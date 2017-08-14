package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettings;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemVirtualObjectBundle;
import com.intellij.openapi.project.Project;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class VirtualConnectionHandler
        implements ConnectionHandler
{
    public static final ConnectionStatus CONNECTION_STATUS = new ConnectionStatus();
    private String id;
    private String name;
    private FileSystemType fileSystemType;
    private double fileSystemVersion;
    private Project project;
    private ConnectionHandlerRef ref;
    private FileSystemObjectBundle objectBundle;

    public VirtualConnectionHandler(String id, String name, FileSystemType fileSystemType,
                                    double fileSystemVersion, Project project)
    {
        this.id = id;
        this.name = name;
        this.project = project;
        this.fileSystemType = fileSystemType;
        this.fileSystemVersion = fileSystemVersion;
        this.ref = new ConnectionHandlerRef(this);
        this.objectBundle = new FileSystemVirtualObjectBundle(this);
    }

    public static ConnectionHandler getDefault(Project project)
    {
        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        return connectionManager.getConnectionBundle().getVirtualConnection("virtual-oracle-fsconnection");
    }

    public ConnectionSettings getSettings()
    {
        return null;
    }

    public void setSettings(ConnectionSettings connectionSettings)
    {
    }
    public FileSystemType getFileSystemType()
    {
        return this.fileSystemType;
    }

    public double getFileSystemVersion()
    {
        return this.fileSystemVersion;
    }

    @NotNull
    public FileSystemObjectBundle getObjectBundle()
    {
        return this.objectBundle;
    }

    public boolean canConnect()
    {
        return false;
    }

    @NotNull
    public Project getProject()
    {
        return this.project;
    }

    public boolean isActive()
    {
        return true;
    }

    public String getId()
    {
        return this.id;
    }

    @NotNull
    public String getName()
    {
       return this.name;
    }

    public String getPresentableText()
    {
        return this.name;
    }


    public String getDescription()
    {
        return "Virtual Hadoop fsconnection";
    }

    public Icon getIcon()
    {
        return Icons.CONNECTION_VIRTUAL;
    }

    public boolean isVirtual()
    {
        return true;
    }

    public boolean isValid()
    {
        return true;
    }
    public boolean isValid(boolean check)
    {
        return true;
    }
    public Filter<FileSystemBrowserTreeNode> getObjectTypeFilter()
    {
        return null;
    }

    public boolean isConnected()
    {
        return false;
    }

    public boolean isDisposed()
    {
        return false;
    }

    @Nullable
    public ConnectionHandler getConnectionHandler()
    {
        return this;
    }


    public String getUserName()
    {
        return "root";
    }

    public boolean createTestConnection()
    {
        return false;
    }

   public FileSystem getMainFileSystem()
   {
       return null;
   }

    public Configuration getMainConnection()
    {
        return null;
    }

    @NotNull
    public ConnectionStatus getConnectionStatus()
    {
        return CONNECTION_STATUS;

    }

    @Nullable
    public ConnectionInfo getConnectionInfo()
    {
        return null;
    }

    public void setConnectionInfo(ConnectionInfo connectionInfo)
    {
    }
    public ConnectionLoadMonitor getLoadMonitor()
    {
        return null;
    }

    public void disconnect()
    {
    }
    public int getIdleMinutes()
    {
        return 0;
    }

    public ConnectionHandlerRef getRef()
    {
        return this.ref;
    }

    public FileSystemInfo getFileSystemInfo()
    {
        return this.fileSystemType.getUrlPatterns()[0].getDefaultInfo();
    }

    public ConnectionHandler clone()
    {
        return null;
    }
    public ConnectionBundle getConnectionBundle()
    {
        return null;
    }

    public void dispose()
    {
    }
}
