package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.ProjectRef;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.list.AbstractFiltrableList;
import com.fangyuzhong.intelliJ.hadoop.core.list.FiltrableList;
import com.fangyuzhong.intelliJ.hadoop.core.list.FiltrableListImpl;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNodeBase;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.FileSystemBrowserTree;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectType;
import com.intellij.navigation.ItemPresentation;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class ConnectionBundle
        extends FileSystemBrowserTreeNodeBase
{

    public static final Filter<ConnectionHandler> ACTIVE_CONNECTIONS_FILTER = new Filter<ConnectionHandler>()
    {
        public boolean accepts(ConnectionHandler connectionHandler)
        {
            return (connectionHandler != null) && (connectionHandler.isActive());
        }
    };
    private ProjectRef projectRef;
    private AbstractFiltrableList<ConnectionHandler> connectionHandlers = new FiltrableListImpl(ACTIVE_CONNECTIONS_FILTER);
    private List<ConnectionHandler> virtualConnections = new ArrayList();

    /**
     * 初始化
     * @param project
     */
    public ConnectionBundle(Project project)
    {
        this.projectRef = new ProjectRef(project);
        this.virtualConnections.add(new VirtualConnectionHandler("virtual-hdfs-fsconnection",
                                                                 "Virtual - hdfs 3.0",
                                                                  FileSystemType.HDFS,
                                                                  3.0, project));
    }

    /**
     * 添加连接到集合
     * @param connectionHandler
     */
    public void addConnection(ConnectionHandler connectionHandler)
    {
        this.connectionHandlers.add(connectionHandler);
        Disposer.register(this, connectionHandler);
    }
    public List<ConnectionHandler> getVirtualConnections()
    {
        return this.virtualConnections;
    }

    /**
     *
     * @param connectionHandlers
     */
    public void setConnectionHandlers(List<ConnectionHandler> connectionHandlers)
    {
        this.connectionHandlers = new FiltrableListImpl<ConnectionHandler>(connectionHandlers, ACTIVE_CONNECTIONS_FILTER);
    }

    public List<ConnectionHandler> getAllConnectionHandlers()
    {
        return this.connectionHandlers.getFullList();
    }

    /**
     *
     * @param id
     * @return
     */
    public ConnectionHandler getConnection(String id)
    {
        for (ConnectionHandler connectionHandler : connectionHandlers.getFullList())
        {
            if (connectionHandler.getId().equals(id)) return connectionHandler;
        }
        return null;
    }

    /**
     *
     * @param id
     * @return
     */
    public ConnectionHandler getVirtualConnection(String id)
    {
        for (ConnectionHandler virtualConnection : this.virtualConnections)
        {
            if (virtualConnection.getId().equals(id))
            {
                return virtualConnection;
            }
        }
        return null;
    }

    @Nullable
    public GenericFileSystemElement getParentElement()
    {
        return null;
    }

    public Icon getIcon(int flags)
    {
        return Icons.PROJECT;
    }

    @NotNull
    public Project getProject()
    {
       return FailsafeUtil.get(this.projectRef.get());
    }


    public boolean isDisposed()
    {
        return false;
    }

    public void dispose()
    {
        super.dispose();
        this.connectionHandlers.clear();
        this.virtualConnections.clear();
    }

    public void navigate(boolean requestFocus)
    {
    }

    public boolean canNavigate()
    {
        return true;
    }

    public boolean canNavigateToSource()
    {
        return false;
    }

    public String getName()
    {
        return getPresentableText();
    }

    public String getLocationString()
    {
        return null;
    }

    public ItemPresentation getPresentation()
    {
        return this;
    }

    public Icon getIcon(boolean open)
    {
        return getIcon(0);
    }

    public boolean isTreeStructureLoaded()
    {
        return true;
    }

    public void initTreeElement()
    {
    }

    public boolean canExpand()
    {
        return true;
    }

    @Nullable
    public FileSystemBrowserTreeNode getParent()
    {
        FileSystemBrowserManager browserManager = FileSystemBrowserManager.getInstance(getProject());
        FileSystemBrowserTree activeBrowserTree = browserManager.getActiveBrowserTree();
        return activeBrowserTree == null ? null : browserManager.isTabbedMode() ? null : activeBrowserTree.getModel().getRoot();
    }

    public List<? extends FileSystemBrowserTreeNode> getChildren()
    {
        return null;
    }

    public void refreshTreeChildren()
    {
        for (ConnectionHandler connectionHandler : this.connectionHandlers)
        {
            connectionHandler.getObjectBundle().refreshTreeChildren();
        }
    }

    public void rebuildTreeChildren()
    {
        for (ConnectionHandler connectionHandler : this.connectionHandlers)
        {
            connectionHandler.getObjectBundle().rebuildTreeChildren();
        }
    }

    public FileSystemBrowserTreeNode getChildAt(int index)
    {
        return this.connectionHandlers.get(index).getObjectBundle();
    }

    public int getChildCount()
    {
        return this.connectionHandlers.size();
    }

    public boolean isLeaf()
    {
        return this.connectionHandlers.size() == 0;
    }

    public int getIndex(FileSystemBrowserTreeNode child)
    {
        FileSystemObjectBundle objectBundle = (FileSystemObjectBundle) child;
        return this.connectionHandlers.indexOf(objectBundle.getConnectionHandler());
    }

    public int getTreeDepth()
    {
        return 1;
    }

    public String getPresentableText()
    {
        return "Hadoop connections";
    }

    public String getPresentableTextDetails()
    {
        int size = this.connectionHandlers.size();
        return "(" + size + ')';
    }

    public String getPresentableTextConditionalDetails()
    {
        return null;
    }

    @Nullable
    public ConnectionHandler getConnectionHandler()
    {
        return null;
    }

    public String getToolTip()
    {
        return "";
    }

    public boolean isEmpty()
    {
        return this.connectionHandlers.getFullList().isEmpty();
    }

    @Nullable
    public DynamicContent getDynamicContent(DynamicContentType dynamicContentType)
    {
        return null;
    }
    public GenericFileSystemElement getUndisposedElement()
    {
        return this;
    }

    public FiltrableList<ConnectionHandler> getConnectionHandlers()
    {
        return connectionHandlers;
    }
}
