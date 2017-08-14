package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.AlreadyDisposedException;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.thread.BackgroundTask;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tree.TreeEventType;
import com.fangyuzhong.intelliJ.hadoop.core.util.CollectionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.core.hadoop.HDFSUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandlerRef;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.BrowserTreeEventListener;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNodeBase;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.LoadInProgressTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.HtmlToolTipBuilder;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.ToolTipProvider;
import com.fangyuzhong.intelliJ.hadoop.fsobject.properties.FileSystemObjectPresentableProperty;
import com.fangyuzhong.intelliJ.hadoop.fsobject.properties.PresentableProperty;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.UpdateLanguageListener;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiInvalidElementAccessException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.*;

/**
 * 文件系统对象的抽象实现
 * Created by fangyuzhong on 17-7-16.
 */
public abstract class FileSystemObjectImpl extends
        FileSystemBrowserTreeNodeBase implements
        FileSystemObject, ToolTipProvider
{
    private List<FileSystemBrowserTreeNode> allPossibleTreeChildren;
    private List<FileSystemBrowserTreeNode> visibleTreeChildren;
    private boolean treeChildrenLoaded;
    protected String name;
    protected FileSystemObjectRef objectRef;
    protected FileSystemObjectRef parentObjectRef;
    private FileSystemObjectProperties properties;
    private FileSystemObjectPsiElement psi;
    private ConnectionHandlerRef connectionHandlerRef;
    private Map<String,String> fileSystemInform;
    /**
     * 初始化文件系统对象
     * @param parentObject 父对象
     * @param HDFSFileStatus HDFS的文件状态对象
     * @param name 对象的名称
     */
    protected FileSystemObjectImpl(FileSystemObject parentObject,
                                   org.apache.hadoop.fs.FileStatus HDFSFileStatus, String name)
    {
        this.connectionHandlerRef = ConnectionHandlerRef.from(parentObject.getConnectionHandler());
        this.parentObjectRef = FileSystemObjectRef.from(parentObject);
        this.name = name;
        init(HDFSFileStatus,null);
    }

    /**
     * 初始化文件系统对象
     * @param connectionHandler 文件对象的连接处理类
     * @param HDFSFileStatus  HDFS的文件状态对象
     * @param name 对象的名称
     */
    protected FileSystemObjectImpl(ConnectionHandler connectionHandler,
                                   org.apache.hadoop.fs.FileStatus HDFSFileStatus, String name)
    {
        this.connectionHandlerRef = ConnectionHandlerRef.from(connectionHandler);
        this.name = name;
        init(HDFSFileStatus,connectionHandler);
    }

    /**
     * 初始化，获取HDFS系统的对象
     * @param HDFSFileStatus
     * @param connectionHandler
     */
    private void init(org.apache.hadoop.fs.FileStatus HDFSFileStatus,ConnectionHandler connectionHandler)
    {
        FileSystemObjectType fileSystemObjectType = HDFSFileStatus.isDirectory()? FileSystemObjectType.DIRECTORY: FileSystemObjectType.FILE;
        this.objectRef = new FileSystemObjectRef(this, fileSystemObjectType,connectionHandler);
        fileSystemInform = HDFSUtil.getFileSystemInformation(HDFSFileStatus);
    }
    /**
     * 获取对象的引用
     * @return
     */
    public FileSystemObjectRef getRef()
    {
        return this.objectRef;
    }
    /**
     * 获取文件对象的属性对象
     * @return
     */
    public FileSystemObjectProperties getProperties()
    {
        if (this.properties == null)
        {
            this.properties = new FileSystemObjectProperties();
        }
        return this.properties;
    }
    /**
     * 获取文件对象的父对象
     * @return
     */
    public FileSystemObject getParentObject()
    {
        return FileSystemObjectRef.get(this.parentObjectRef);
    }
    /**
     *获取默认的导航对象
     * @return
     */
    @Nullable
    public FileSystemObject getDefaultNavigationObject()
    {
        return null;
    }
    /**
     *
     * @return
     */
    @Nullable
    public GenericFileSystemElement getParentElement()
    {
        return getParentObject();
    }

    /**
     *获取文件对象类型
     * @return
     */
    public String getTypeName()
    {
        return getObjectType().getName();
    }

    /**
     *获取文件对象的名称
     * @return
     */
    @NotNull
    public String getName()
    {
        return this.name;
    }

    /**
     *判断对象是否已经加载过
     * @return
     */
    public int getOverload()
    {
        return 0;
    }


    /**
     *获取图像的图标
     * @return
     */
    @Nullable
    public Icon getIcon()
    {
        return getObjectType().getIcon();
    }

    /**
     *
     * @return
     */
    public String getQualifiedName()
    {
        return this.objectRef.getPath();
    }


    /**
     *获取提示文字
     * @return
     */
    public String getToolTip()
    {
        if (isDisposed())
        {
            return null;
        }
        return new HtmlToolTipBuilder()
        {
            public void buildToolTip()
            {
                FileSystemObjectImpl.this.buildToolTip(this);
            }
        }.getToolTip();
    }

    /**
     *
     * @param ttb
     */
    public void buildToolTip(HtmlToolTipBuilder ttb)
    {
        ConnectionHandler connectionHandler = getConnectionHandler();
        ttb.append(true, getQualifiedName(), false);
        ttb.append(true, LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.URLNAME), "-2", null, false);
        ttb.append(false, connectionHandler.getPresentableText(), false);
    }


    /**
     *获取对象连接的对象集合
     * @return
     */
    @NotNull
    public FileSystemObjectBundle getObjectBundle()
    {
        ConnectionHandler connectionHandler = getConnectionHandler();
        return connectionHandler.getObjectBundle();
    }

    /**
     *获取对象的连接
     * @return
     */
    @NotNull
    public ConnectionHandler getConnectionHandler()
    {
        return ConnectionHandlerRef.get(this.connectionHandlerRef);
    }

    /**
     *
     * @return
     */
    @Nullable
    public FileSystemObject getUndisposedElement()
    {
        return this.objectRef.get();
    }

    /**
     *
     * @param dynamicContentType
     * @return
     */
    @Nullable
    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType)
    {
        return null;
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
     *
     * @return
     */
    public String getLocationString()
    {
       return null;
    }

    /**
     *
     * @param open
     * @return
     */
    public Icon getIcon(boolean open)
    {
        return getIcon();
    }

    /**
     *
     */
    public void initTreeElement(){}

    /**
     *
     * @return
     */
    public boolean isTreeStructureLoaded()
    {
        return this.treeChildrenLoaded;
    }

    /**
     *
     * @return
     */
    public boolean canExpand()
    {
        return (!isLeaf()) && (this.treeChildrenLoaded) && (getChildAt(0).isTreeStructureLoaded());
    }

    /**
     *
     * @param flags
     * @return
     */
    public Icon getIcon(int flags)
    {
        return getIcon();
    }

    /**
     *获取显示名称
     * @return
     */
    public String getPresentableText()
    {
        if(this.getObjectType()== FileSystemObjectType.DIRECTORY)
        {
            return this.name+"("+this.getChildCount()+")";
        }
        else
        {
            return this.name;
        }
    }

    /**
     *
     * @return
     */
    public String getPresentableTextDetails()
    {
        return null;
    }

    /**
     *
     * @return
     */
    public String getPresentableTextConditionalDetails()
    {
        return null;
    }

    /**
     *获取父节点
     * @return
     */
    @NotNull
    public FileSystemBrowserTreeNode getParent()
    {
        if (this.parentObjectRef != null)
        {
            FileSystemObject object = this.parentObjectRef.get();
            if (object != null)
            {
                return object;
            }
        }
        else
        {
            FileSystemObjectBundle objectBundle = getObjectBundle();
            return  objectBundle;
        }
        throw AlreadyDisposedException.INSTANCE;
    }

    /**
     *
     * @return
     */
    public int getTreeDepth()
    {
        FileSystemBrowserTreeNode treeParent = getParent();
        return treeParent.getTreeDepth() + 1;
    }

    /**
     *
     * @param isreBuild
     * @return
     */
    @NotNull
    public List<FileSystemBrowserTreeNode> getAllPossibleTreeChildren(boolean isreBuild)
    {
        if(isreBuild)
        {
         this.allPossibleTreeChildren.clear();
            synchronized (this)
            {
                this.allPossibleTreeChildren = buildAllPossibleTreeChildren();
            }
        }
        else
        {
            if (this.allPossibleTreeChildren == null)
            {
                synchronized (this)
                {
                    if (this.allPossibleTreeChildren == null)
                    {
                        this.allPossibleTreeChildren = buildAllPossibleTreeChildren();
                    }
                }
            }
        }
        return this.allPossibleTreeChildren;
    }

    /**
     *
     * @return
     */
    public List<? extends FileSystemBrowserTreeNode> getChildren()
    {
        if (this.visibleTreeChildren == null)
        {
            synchronized (this)
            {
                if (this.visibleTreeChildren == null)
                {
                    this.visibleTreeChildren = new ArrayList();
                    this.visibleTreeChildren.add(new LoadInProgressTreeNode(this));
                    ConnectionHandler connectionHandler = getConnectionHandler();
                    String connectionString = connectionHandler == null ? "" : " (" + connectionHandler.getName() + ")";
                    new BackgroundTask(getProject(), "Loading data " + connectionString, true)
                    {
                        public void execute(@NotNull ProgressIndicator progressIndicator)
                        {
                            buildTreeChildren(false);
                        }
                    }.start();
                }
            }
        }
        return this.visibleTreeChildren;
    }

    /**
     *
     * @param isreBuild
     */
    private void buildTreeChildren(boolean isreBuild)
    {
        FailsafeUtil.check(this);
        List<FileSystemBrowserTreeNode> allPossibleTreeChildren = getAllPossibleTreeChildren(isreBuild);
        List<FileSystemBrowserTreeNode> newTreeChildren = allPossibleTreeChildren;
        if (allPossibleTreeChildren.size() > 0)
        {

            newTreeChildren = new ArrayList(newTreeChildren);
            for (FileSystemBrowserTreeNode treeNode : newTreeChildren)
            {
                FileSystemObject objectList = (FileSystemObject) treeNode;
                objectList.initTreeElement();
                FailsafeUtil.check(this);
            }
            if ((this.visibleTreeChildren.size() == 1) && ((this.visibleTreeChildren.get(0) instanceof LoadInProgressTreeNode)))
            {
                this.visibleTreeChildren.get(0).dispose();
            }
        }
        this.visibleTreeChildren = newTreeChildren;
        this.treeChildrenLoaded = true;
        //通知TreeNode修改，需要重新加载
        Project project = FailsafeUtil.get(getProject());
        ( EventUtil.notify(project, BrowserTreeEventListener.TOPIC)).nodeChanged(this, TreeEventType.STRUCTURE_CHANGED);
        FileSystemBrowserManager.scrollToSelectedElement(getConnectionHandler());
    }

    /**
     *
     */
    public void refreshTreeChildren()
    {
        rebuildTreeChildren();
    }

    /**
     *
     */
    public void rebuildTreeChildren()
    {
        buildTreeChildren(true);

    }
    /**
     *
     * @return
     */
    public abstract List<FileSystemBrowserTreeNode> buildAllPossibleTreeChildren();

    /**
     *
     * @return
     */
    public boolean isLeaf()
    {
        if (this.visibleTreeChildren == null)
        {
            List<FileSystemBrowserTreeNode> allTreeChilder = getAllPossibleTreeChildren(false);
            if(allTreeChilder==null) return true;
            for (FileSystemBrowserTreeNode treeNode : getAllPossibleTreeChildren(false))
            {
                if (treeNode != null)
                {
                    return false;
                }
            }
            return true;
        }
        return this.visibleTreeChildren.size() == 0;
    }

    /**
     *
     * @param index
     * @return
     */
    public FileSystemBrowserTreeNode getChildAt(int index)
    {
        return getChildren().get(index);
    }

    /**
     *
     * @return
     */
    public int getChildCount()
    {
        return getChildren().size();
    }

    /**
     *
     * @param child
     * @return
     */
    public int getIndex(FileSystemBrowserTreeNode child)
    {
        return getChildren().indexOf(child);
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj)
    {
        if (obj == this)
        {
            return true;
        }
        if ((obj instanceof FileSystemObject))
        {
            FileSystemObject object = (FileSystemObject) obj;
            return this.objectRef.equals(object.getRef());
        }
        return false;
    }

    /**
     *
     * @return
     */
    public int hashCode()
    {
        return this.objectRef.hashCode();
    }

    /**
     *
     * @return
     * @throws PsiInvalidElementAccessException
     */
    @NotNull
    public Project getProject()
            throws PsiInvalidElementAccessException
    {
        ConnectionHandler connectionHandler = FailsafeUtil.get(getConnectionHandler());
        return connectionHandler.getProject();
    }

    /**
     *
     * @param o
     * @return
     */
    public int compareTo(@NotNull Object o)
    {
        if ((o instanceof FileSystemObject))
        {
            FileSystemObject object = (FileSystemObject) o;
            return this.objectRef.compareTo(object.getRef());
        }
        return -1;
    }

    /**
     *
     * @return
     */
    public String toString()
    {
        return this.name;
    }

    /**
     *
     * @return
     */
    public boolean isValid()
    {
        return !isDisposed();
    }

    /**
     *
     */
    public void dispose()
    {
        if (!isDisposed())
        {
            super.dispose();
            CollectionUtil.clearCollection(this.visibleTreeChildren);
            CollectionUtil.clearCollection(this.allPossibleTreeChildren);
        }
    }

    /**
     *
     * @return
     */
    public String getDescription()
    {
        return getQualifiedName();
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
     * @return
     */
    public List<PresentableProperty> getPresentableProperties()
    {
        List<PresentableProperty> properties = new ArrayList();
        if(fileSystemInform!=null)
        {
            for (Map.Entry<String, String> entry : fileSystemInform.entrySet())
            {
                properties.add(new FileSystemObjectPresentableProperty(entry.getKey(), entry.getValue()));
            }
        }
        return properties;
    }
}
