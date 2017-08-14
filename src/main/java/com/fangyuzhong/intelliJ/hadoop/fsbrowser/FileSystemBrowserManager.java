package com.fangyuzhong.intelliJ.hadoop.fsbrowser;

import com.fangyuzhong.intelliJ.hadoop.core.AbstractProjectComponent;
import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.fangyuzhong.intelliJ.hadoop.core.util.DisposableLazyValue;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.LazyValue;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionBundleSettingsListener;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettingsAdapter;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettingsListener;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.BrowserTreeModel;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.TabbedBrowserTreeModel;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.BrowserToolWindowForm;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.FileSystemBrowserTree;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.components.StorageScheme;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.impl.ProjectLifecycleListener;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 定义文件系统管理类。Project级别的插件
 * Created by fangyuzhong on 17-7-16.
 */
@State(name = "HadoopNavigator.Project.FileSystemBrowserManager",
        storages = {@Storage(file = "$PROJECT_CONFIG_DIR$/hdfsnavigator.xml", scheme = StorageScheme.DIRECTORY_BASED),
                    @Storage(file = "$PROJECT_FILE$")})
public class FileSystemBrowserManager
        extends AbstractProjectComponent
        implements PersistentStateComponent<Element>
{
    public static final String TOOL_WINDOW_ID = "Hadoop FileSystem";
    private boolean objectIsShow=true;
    public static final ThreadLocal<Boolean> AUTOSCROLL_FROM_EDITOR = new ThreadLocal();
    private LazyValue<BrowserToolWindowForm> toolWindowForm = new DisposableLazyValue(this)
    {
        protected BrowserToolWindowForm load()
        {
            return new BrowserToolWindowForm(FileSystemBrowserManager.this.getProject());
        }
    };

    /**
     * 获取文件系统对象是否显示
     * @return
     */
    public boolean getObjectIsShow()
    {
        return objectIsShow;
    }
    /*

     */
    private FileSystemBrowserManager(Project project)
    {
        super(project);
    }

    /**
     * 获取活动的BrowserTree对象
     * @return
     */
    @Nullable
    public FileSystemBrowserTree getActiveBrowserTree()
    {
        return getToolWindowForm().getActiveBrowserTree();
    }

    /**
     * 获取活动的连接对象
     * @return
     */
    @Nullable
    public ConnectionHandler getActiveConnection()
    {
        FileSystemBrowserTree activeBrowserTree = getActiveBrowserTree();
        if (activeBrowserTree != null)
        {
            BrowserTreeModel browserTreeModel = activeBrowserTree.getModel();
            if ((browserTreeModel instanceof TabbedBrowserTreeModel))
            {
                TabbedBrowserTreeModel tabbedBrowserTreeModel = (TabbedBrowserTreeModel) browserTreeModel;
                return tabbedBrowserTreeModel.getConnectionHandler();
            }
            FileSystemBrowserTreeNode browserTreeNode = activeBrowserTree.getSelectedNode();
            if (browserTreeNode != null)
            {
                return browserTreeNode.getConnectionHandler();
            }
        }
        return null;
    }

    /**
     * 获取浮动面板对象
     * @return
     */
    @NotNull
    public ToolWindow getBrowserToolWindow()
    {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(getProject());
        return toolWindowManager.getToolWindow(TOOL_WINDOW_ID);
    }
    /**
     * 获取主窗体对象
     * @return
     */
    @NotNull
    public BrowserToolWindowForm getToolWindowForm()
    {
        return this.toolWindowForm.get();
    }
    /**
     *
     * @return
     */
    public String toString()
    {
        return "HadoopFileSystem Browser";
    }

    /**
     *导航到选中的TreeNode
     * @param treeNode
     * @param focus
     * @param scroll
     */
    public void navigateToElement(FileSystemBrowserTreeNode treeNode, boolean focus, boolean scroll)
    {
        ToolWindow toolWindow = getBrowserToolWindow();

        toolWindow.show(null);
        if (treeNode != null)
        {
            getToolWindowForm().getBrowserForm().selectElement(treeNode, focus, scroll);
        }
    }
    /**
     * 获取FileSystemBrowserManager对象
     * @param project
     * @return
     */
    public static FileSystemBrowserManager getInstance(@NotNull Project project)
    {
        return FailsafeUtil.getComponent(project, FileSystemBrowserManager.class);
    }

    /**
     * 获取插件组件的名称
     * @return
     */
    @NonNls
    @NotNull
    public String getComponentName()
    {
        return "HadoopNavigator.Project.FileSystemBrowserManager";
    }

    /**
     * 插件初始化
     */
    public void initComponent()
    {
        //注册监听器
        EventUtil.subscribe(null, ProjectLifecycleListener.TOPIC, this.projectLifecycleListener);
    }


    /*
    Project项目生命周期监听处理
     */
    private ProjectLifecycleListener projectLifecycleListener = new ProjectLifecycleListener()
    {
        @Override
        public void projectComponentsInitialized(@NotNull Project project)
        {
            //注册HDFS连接集合监听器
            EventUtil.subscribe(project,null, ConnectionBundleSettingsListener.TOPIC,this.connectionBundleSettingsListener);
        }

        private ConnectionBundleSettingsListener connectionBundleSettingsListener  = new ConnectionBundleSettingsListener()
        {
            /**
             * 重新构建UI
             */
            @Override
            public void settingsChanged()
            {
                getToolWindowForm().rebuild();
            }
        };
    };


    /**
     *设置滚动选中元素
     * @param connectionHandler
     */
    public static void scrollToSelectedElement(ConnectionHandler connectionHandler)
    {
        if ((connectionHandler != null) && (!connectionHandler.isDisposed()))
        {
            FileSystemBrowserManager browserManager = getInstance(connectionHandler.getProject());
            BrowserToolWindowForm toolWindowForm = browserManager.getToolWindowForm();
            FileSystemBrowserTree browserTree = toolWindowForm.getBrowserTree(connectionHandler);
            if ((browserTree != null) && (browserTree.getTargetSelection() != null))
            {
                new SimpleLaterInvocator()
                {
                    protected void execute()
                    {
                        browserTree.scrollToSelectedElement();
                    }
                }.start();
            }
        }
    }

    /**
     *
     * @return
     */
    public boolean isTabbedMode()
    {
        return true;
    }

    /**
     *
     * @return
     */
    public Filter<FileSystemBrowserTreeNode> getObjectTypeFilter()
    {
        return null;
    }


    /**
     *设置是否显示文件对象属性UI
     * @param visible
     */
    public void showObjectProperties(boolean visible)
    {
        BrowserToolWindowForm toolWindowForm = getToolWindowForm();
        if (visible)
        {
            toolWindowForm.showObjectProperties();
            objectIsShow=true;
        } else
        {
            toolWindowForm.hideObjectProperties();
            objectIsShow=false;
        }
    }

    /**
     *
     * @return
     */
    @Nullable
    public Element getState()
    {
        Element element = new Element("state");
        return element;
    }

    /**
     *
     * @param element
     */
    public void loadState(Element element)
    {

    }
}
