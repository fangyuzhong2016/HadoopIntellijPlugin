package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.thread.BackgroundTask;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tree.TreeEventType;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserUtils;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.BrowserTreeEventListener;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNodeBase;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.LoadInProgressTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.HtmlToolTipBuilder;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.UpdateLanguageListener;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 *实现文件对象集合的实现
 * Created by fangyuzhong on 17-7-17.
 */
public class FileSystemObjectBundleImpl extends
        FileSystemBrowserTreeNodeBase implements
        FileSystemObjectBundle
{
    private ConnectionHandler connectionHandler;
    private FileSystemBrowserTreeNode treeParent;
    private List<FileSystemBrowserTreeNode> allPossibleTreeChildren;
    private List<FileSystemBrowserTreeNode> visibleTreeChildren;
    private boolean treeChildrenLoaded;
    private FileSystem fileSystem;

    /**
     * 初始化
     * @param connectionHandler 连接处理接口
     * @param treeParent 父节点
     */
    public FileSystemObjectBundleImpl(ConnectionHandler connectionHandler, FileSystemBrowserTreeNode treeParent)
    {
        this.connectionHandler = connectionHandler;
        this.treeParent = treeParent;
        loadData();
    }
    /**
     * 重新加载数据
     */
    public void loadData()
    {
        //获取根节点下的所有文件和目录 创建 节点
        fileSystem = connectionHandler.getMainFileSystem();
        if(connectionHandler.getConnectionStatus().isConnected())
        {
            try
            {
                if( this.allPossibleTreeChildren!=null)
                {
                    this.allPossibleTreeChildren.clear();
                }
                FileStatus[] fileStatuses = fileSystem.listStatus(new Path("/"));
                FileSystemBrowserTreeNode[] fileSystemBrowserTreeNodes = new FileSystemBrowserTreeNode[fileStatuses.length];
                for (int i = 0; i < fileStatuses.length; i++)
                {
                    FileStatus f = fileStatuses[i];
                    if (f.isDirectory())
                    {
                        //如果是目录，实例化目录对象
                        FileSystemObject dirdbObject = new HadoopDirectoryObject(connectionHandler, f, f.getPath().getName());
                        fileSystemBrowserTreeNodes[i] = dirdbObject;
                    } else
                    {
                        //如果是文件，实例化文件对象
                        FileSystemObject filedbObject = new HadoopFileObject(connectionHandler, f, f.getPath().getName());
                        fileSystemBrowserTreeNodes[i] = filedbObject;
                    }
                }
                this.allPossibleTreeChildren = FileSystemBrowserUtils.createList(fileSystemBrowserTreeNodes);
            }
            catch (Exception ex)
            {

            }
        }
        else
        {
            if( this.allPossibleTreeChildren!=null)
            {
                this.allPossibleTreeChildren.clear();
            }
        }
    }

    /**
     * 返回目录路径
     * @return
     */
    public String getLocationString()
    {
        return "/";
    }

    /**
     * 获取显示的名称
     * @return
     */
    public String getName()
    {
        return getPresentableText();
    }

    @Nullable
    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType)
    {
        return null;
    }

    /**
     * 获取根节点所在的连接
     * @return
     */
    @NotNull
    public ConnectionHandler getConnectionHandler()
    {
        return FailsafeUtil.get(this.connectionHandler);
    }

    /*********************************************************
     *                     实现Tree的相关方法                       *
     *********************************************************/
    /**
     * 判断是否是叶子节点
     * @return
     */
    public boolean isLeaf()
    {
        return false;
    }

    /**
     * 获取子节点的数量
     * @return
     */
    public int getChildCount()
    {
        return  getChildren()==null?0:getChildren().size();
    }

    /**
     *
     * @return
     */
    public ItemPresentation getPresentation()
    {
        return this;
    }

    /**
     * 获取鼠标悬停提示的文字
     * @return
     */
    public String getToolTip()
    {
        return new HtmlToolTipBuilder()
        {
            public void buildToolTip()
            {
                append(true, LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.CONNECTIONTIPTEXT), true);
                ConnectionHandler connectionHandler = getConnectionHandler();
                if (connectionHandler.getConnectionStatus().isConnected())
                {
                    append(false, " - "+LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.ACTIVETEXT), true);
                } else if (connectionHandler.canConnect() && !connectionHandler.isValid())
                {
                    append(false, " - invalid", true);
                    append(true, connectionHandler.getConnectionStatus().getStatusMessage(), "-2", "red", false);
                }
                createEmptyRow();

                append(true, connectionHandler.getProject().getName(), false);
                append(false, "/", false);
                append(false, connectionHandler.getName(), false);
                append(true, "Pool size: ", "-2", null, false);
                append(false, " (", false);
                append(false, ")", false);
            }
        }.getToolTip();
    }

    /**
     * 初始化树的元素
     */
    public void initTreeElement(){}

    /**
     * 是否展开
     * @return
     */
    public boolean canExpand()
    {
        return (this.treeChildrenLoaded) && (getChildAt(0).isTreeStructureLoaded());
    }

    /**
     * 获取树的深度
     * @return
     */
    public  int getTreeDepth()
    {
        return this.treeParent == null ? 0 : this.treeParent.getTreeDepth() + 1;
    }

    /**
     * 树结构是否加载
     * @return
     */
    public boolean isTreeStructureLoaded()
    {
        return this.treeChildrenLoaded;
    }

    /**
     * 获取节点的子节点
     * @return
     */
    public  List<? extends FileSystemBrowserTreeNode> getChildren()
    {
        if(connectionHandler.canConnect())
        {
            if (this.visibleTreeChildren == null)
            {
                synchronized (this)
                {
                    if (this.visibleTreeChildren == null)
                    {
                        this.visibleTreeChildren = new ArrayList();
                        this.visibleTreeChildren.add(new LoadInProgressTreeNode(this));
                    }
                    ConnectionHandler connectionHandler = getConnectionHandler();

                    String connectionString = connectionHandler == null ? "" : " (" + connectionHandler.getName() + ")";
                    new BackgroundTask(getProject(), "Loading data dictionary" + connectionString, true)
                    {
                        public void execute(@NotNull ProgressIndicator progressIndicator)
                        {
                            buildTreeChildren();
                        }
                    }.start();

                }
            }
        }
        return this.visibleTreeChildren;
    }

    /**
     * 刷新子节点
     */
    public void refreshTreeChildren()
    {
        loadData();
        buildTreeChildren();
    }

    /**
     * 重构该节点的子节点
     */
    public void rebuildTreeChildren()
    {
        if (this.visibleTreeChildren != null)
        {
            for (FileSystemBrowserTreeNode treeNode : this.visibleTreeChildren)
            {
                treeNode.rebuildTreeChildren();
            }
        }
    }

    /**
     * 获取节点图标
     * @param paramInt
     * @return
     */
    public  Icon getIcon(int paramInt)
    {
        return getConnectionHandler().getIcon();
    }

    /**
     * 获取节点的图标
     * @param open
     * @return
     */
    public Icon getIcon(boolean open)
    {
        return getIcon(0);
    }

    /**
     *获取节点文本
     * @return
     */
    public String getPresentableText()
    {
        String message = "(未连接)";
        if(getConnectionHandler().isConnected())
        {
            message="";
        }
        return getConnectionHandler().getName()+message;
    }

    /**
     * 获取节点文本详细信息
     * @return
     */
    public String getPresentableTextDetails()
    {
        return getConnectionHandler().getPresentableText();
    }

    /**
     *获取节点显示的详细信息
     * @return
     */
    public String getPresentableTextConditionalDetails()
    {
        return "";
    }

    /**
     * 获取子节点
     * @param paramInt
     * @return
     */
    public FileSystemBrowserTreeNode getChildAt(int paramInt)
    {
        return getChildren().get(paramInt);
    }

    /**
     * 获取父节点
     * @return
     */
    public FileSystemBrowserTreeNode getParent()
    {
        return this.treeParent;
    }

    /**
     * 获取节点所在的索引
     * @param paramBrowserTreeNode
     * @return
     */
    public int getIndex(FileSystemBrowserTreeNode paramBrowserTreeNode)
    {
        return getChildren().indexOf(paramBrowserTreeNode);
    }

    /**
     *
     * @return
     */
    @NotNull
    public Project getProject()
    {
        return getConnectionHandler().getProject();
    }

    /**
     *
     * @return
     */
    @Nullable
    public GenericFileSystemElement getParentElement()
    {
        return null;
    }

    /**
     *
     * @return
     */
    public GenericFileSystemElement getUndisposedElement()
    {
        return this;
    }

    /**
     *
     * @return
     */
    public boolean canNavigate()
    {
        return true;
    }

    /**
     *
     * @return
     */
    public boolean canNavigateToSource()
    {
        return false;
    }

    /**
     *
     * @param requestFocus
     */
    public void navigate(boolean requestFocus)
    {
        FileSystemBrowserManager browserManager = FileSystemBrowserManager.getInstance(getProject());
        browserManager.navigateToElement(this, requestFocus, true);
    }

    /**
     * 构建子节点
     */
    private void buildTreeChildren()
    {

        FailsafeUtil.check(this);
        List<FileSystemBrowserTreeNode> newTreeChildren = this.allPossibleTreeChildren;
        if(newTreeChildren==null) return;
        for (FileSystemBrowserTreeNode treeNode : newTreeChildren)
        {
            FileSystemObject fileSystemObject =(FileSystemObject) treeNode;
            fileSystemObject.initTreeElement();
            FailsafeUtil.check(this);
        }
        if(this.visibleTreeChildren!=null)
        {
            if ((this.visibleTreeChildren.size() == 1) && ((this.visibleTreeChildren.get(0) instanceof LoadInProgressTreeNode)))
            {
                this.visibleTreeChildren.get(0).dispose();
            }
        }
        this.visibleTreeChildren = newTreeChildren;
        this.treeChildrenLoaded = true;
        Project project = FailsafeUtil.get(getProject());
        (EventUtil.notify(project, BrowserTreeEventListener.TOPIC)).nodeChanged(this, TreeEventType.STRUCTURE_CHANGED);
        FileSystemBrowserManager.scrollToSelectedElement(getConnectionHandler());
    }
}
