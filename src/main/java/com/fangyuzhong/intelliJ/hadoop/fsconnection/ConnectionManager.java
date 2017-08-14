package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.AbstractProjectComponent;
import com.fangyuzhong.intelliJ.hadoop.core.Constants;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.MessageUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.TimeUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionBundleSettings;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionFileSystemSettings;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettingsAdapter;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettingsListener;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定义HDFS连接管理类，支持持久化配置
 * Created by fangyuzhong on 17-7-15.
 */

@State(name = "HadoopNavigator.Project.ConnectionManager",
        storages = {@com.intellij.openapi.components.Storage(file = "$PROJECT_CONFIG_DIR$/hdfsnavigator.xml",
                        scheme = com.intellij.openapi.components.StorageScheme.DIRECTORY_BASED),
                    @com.intellij.openapi.components.Storage(file = "$PROJECT_FILE$")})
public class ConnectionManager
        extends AbstractProjectComponent
        implements PersistentStateComponent<Element>
{
    private Timer idleConnectionCleaner;
    private ConnectionBundle connectionBundle;
    private static ConnectionHandlerRef lastUsedConnection;

    /**
     * 获取ConnectionManager对象
     * @param project
     * @return
     */
    public static ConnectionManager getInstance(@NotNull Project project)
    {
        return getComponent(project);
    }

    /**
     *获取ConnectionManager对象
     * @param project
     * @return
     */
    private static ConnectionManager getComponent(@NotNull Project project)
    {

        return FailsafeUtil.getComponent(project, ConnectionManager.class);
    }

    /**
     * 初始化
     * @param project
     */
    private ConnectionManager(Project project)
    {
        super(project);
        this.connectionBundle = ConnectionBundleSettings.getInstance(getProject()).getConnectionBundle();
        Disposer.register(this, this.connectionBundle);
    }


    /**
     * 初始化加载
     */
    public void initComponent()
    {
        super.initComponent();
        Project project = getProject();
        //注册连接配置修改监听
        EventUtil.subscribe(project, this, ConnectionSettingsListener.TOPIC, this.connectionSettingsListener);
        this.idleConnectionCleaner = new Timer("HDFS - Idle Connection Cleaner [" + project.getName() + "]");
        this.idleConnectionCleaner.schedule(new CloseIdleConnectionTask(), TimeUtil.ONE_MINUTE, TimeUtil.ONE_MINUTE);
    }

    /*
    连接配置修改后处理
     */
    private ConnectionSettingsListener connectionSettingsListener = new ConnectionSettingsAdapter()
    {
        public void connectionChanged(String connectionId)
        {
            final ConnectionHandler connectionHandler = getConnectionHandler(connectionId);
            connectionHandler.getObjectBundle().refreshTreeChildren();
        }
    };

    public void disposeConnections(@NotNull List<ConnectionHandler> connectionHandlers)
    {
        final Project project = getProject();
        final ArrayList<ConnectionHandler> disposeList = new ArrayList<ConnectionHandler>(connectionHandlers);
        connectionHandlers.clear();
    }

    public void dispose()
    {
        ConnectionBundle connectionBundle = getConnectionBundle();
        super.dispose();
        if (this.idleConnectionCleaner != null)
        {
            this.idleConnectionCleaner.cancel();
            this.idleConnectionCleaner.purge();
        }
        Disposer.dispose(connectionBundle);
    }

    public ConnectionBundle getConnectionBundle()
    {
        return this.connectionBundle;
    }


    @Nullable
    public ConnectionHandler getConnectionHandler(String connectionId)
    {
        for (ConnectionHandler connectionHandler : getConnectionBundle().getConnectionHandlers().getFullList())
        {
            if (connectionHandler.getId().equals(connectionId))
            {
                return connectionHandler;
            }
        }
        return null;
    }

    public List<ConnectionHandler> getConnectionHandlers()
    {
        return getConnectionBundle().getConnectionHandlers();
    }

    public ConnectionHandler getActiveConnection(Project project)
    {
        ConnectionHandler connectionHandler = null;

        return connectionHandler;
    }


    private class CloseIdleConnectionTask
            extends TimerTask
    {
        private CloseIdleConnectionTask()
        {
        }

        public void run()
        {

        }
    }

    public boolean canExitApplication()
    {
        return true;
    }


    @NonNls
    @NotNull
    public String getComponentName()
    {
        return "HadoopNavigator.Project.ConnectionManager";

    }

    @Nullable
    public Element getState()
    {
        return null;
    }

    public void loadState(Element element)
    {
    }

    /**
     * 测试HDFS连接
     * @param fileSystemSettings
     * @param showMessageDialog
     */
    public static void testConfigConnection(ConnectionFileSystemSettings fileSystemSettings,
                                            boolean showMessageDialog)
    {
        Project project = fileSystemSettings.getProject();
        boolean isCanConnection=false;
        ClassLoader pClassLoader = null;
        Configuration configurationHdfs=null;
        FileSystem fileSystem=null;
        try
        {
            String hdfsPath = fileSystemSettings.getHDFSUrl();
            String yarnResourceAMPath = fileSystemSettings.getMapReducelUrl();
            configurationHdfs = new Configuration(false);
            pClassLoader = Class.forName(com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager.class.getName()).getClassLoader();
            configurationHdfs.setClassLoader(pClassLoader);
            //设置HDFS相关属性
            configurationHdfs.set(Constants.FS_HDFS_IMPL_KEY, Constants.FS_HDFS_IMPL_VALUE);
            configurationHdfs.set(Constants.FS_FILE_IMPL_KEY,Constants.FS_FILE_IMPL_VALUE);
            configurationHdfs.set(Constants.FS_DEFAULTFS_KEY, hdfsPath);
            configurationHdfs.set(Constants.YARN_RESOURCEMANAGER_SCHEDULER_ADDRESS,yarnResourceAMPath);

            fileSystem = FileSystem.get(configurationHdfs);
            isCanConnection= fileSystem.exists(fileSystem.getWorkingDirectory());

        }catch (Exception e)
        {
            isCanConnection=false;
        }
        finally
        {
            configurationHdfs=null;
            fileSystem=null;
        }
        if (showMessageDialog)
        {
            String strTilte = LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.MESSAGETILE);
            String strmessage="";
            if(isCanConnection)
            {
                strmessage=LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGCONNECTIONTESTSUCCESS);
            }
            else
            {
                strmessage=LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGCONNECTIONTESTFAILED);
            }
            MessageUtil.showInfoDialog(project,strTilte,strmessage);
        }
    }
}
