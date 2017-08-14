package com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.content.DatabaseLoadMonitor;
import com.fangyuzhong.intelliJ.hadoop.core.thread.BackgroundTask;
import com.fangyuzhong.intelliJ.hadoop.core.thread.ModalTask;
import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.fangyuzhong.intelliJ.hadoop.core.ui.GUIUtil;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tree.FileSystemTree;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.action.ConnectionActionGroup;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserUtils;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.TreeNavigationHistory;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.*;
import com.fangyuzhong.intelliJ.hadoop.fsobject.*;
import com.fangyuzhong.intelliJ.hadoop.fsobject.action.ObjectActionGroup;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPopupMenu;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.util.ui.tree.TreeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;

/**
 * 定义文件系统对象浏览的树控件
 * Created by fangyuzhong on 17-7-16.
 */
public class FileSystemBrowserTree extends FileSystemTree
{
    private FileSystemBrowserTreeNode targetSelection;
    private JPopupMenu popupMenu;
    private TreeNavigationHistory navigationHistory = new TreeNavigationHistory();

    /**
     * 初始化树控件
     * @param treeModel
     */
    public FileSystemBrowserTree(BrowserTreeModel treeModel)
    {
        super(treeModel);
        //注册监听事件
        addKeyListener(this.keyListener);
        addMouseListener(this.mouseListener);
        addTreeSelectionListener(this.treeSelectionListener);
        setToggleClickCount(0);
        setRootVisible(treeModel instanceof TabbedBrowserTreeModel);
        setShowsRootHandles(true);
        setAutoscrolls(true);
        //设置TreeNode的渲染器
        FileSystemBrowserTreeCellRenderer browserTreeCellRenderer = new FileSystemBrowserTreeCellRenderer(treeModel.getProject());
        setCellRenderer(browserTreeCellRenderer);
        FileSystemBrowserTreeSpeedSearch speedSearch = new FileSystemBrowserTreeSpeedSearch(this);
        //注册相关需要释放的资源对象
        Disposer.register(this, speedSearch);
        Disposer.register(this, treeModel);
        Disposer.register(this, this.navigationHistory);
    }

    /**
     * 获取Project
     * @return
     */
    public Project getProject()
    {
        return getModel().getProject();
    }

    /**
     * 获取TreeModel
     * @return
     */
    public BrowserTreeModel getModel()
    {
        return (BrowserTreeModel) super.getModel();
    }

    /**
     * 获取导航历史对象
     * @return
     */
    public TreeNavigationHistory getNavigationHistory()
    {
        return this.navigationHistory;
    }

    /**
     * 设置HDFS连接节点展开
     */
    public void expandConnectionManagers()
    {
        new SimpleLaterInvocator()
        {
            protected void execute()
            {
                ConnectionManager connectionManager = ConnectionManager.getInstance(FileSystemBrowserTree.this.getProject());
                ConnectionBundle connectionBundle = connectionManager.getConnectionBundle();
                TreePath treePath = FileSystemBrowserUtils.createTreePath(connectionBundle);
                FileSystemBrowserTree.this.setExpandedState(treePath, true);
            }
        }.start();
    }

    /**
     * 选择树元素
     * @param treeNode
     * @param focus
     */
    public void selectElement(FileSystemBrowserTreeNode treeNode, boolean focus)
    {
        ConnectionHandler connectionHandler = treeNode.getConnectionHandler();
        Filter<FileSystemBrowserTreeNode> filter = connectionHandler == null ? FileSystemBrowserManager.getInstance(getProject()).getObjectTypeFilter() : connectionHandler.getObjectTypeFilter();
        if (filter.accepts(treeNode))
        {
            this.targetSelection = treeNode;
            scrollToSelectedElement();
            if (focus)
            {
                requestFocus();
            }
        }
    }

    /**
     *滚动选中的树元素
     */
    public synchronized void scrollToSelectedElement()
    {
        if ((getProject().isOpen()) && (this.targetSelection != null))
        {
            this.targetSelection = ((FileSystemBrowserTreeNode) this.targetSelection.getUndisposedElement());
            TreePath treePath = FileSystemBrowserUtils.createTreePath(this.targetSelection);
            if (treePath != null)
            {
                for (Object object : treePath.getPath())
                {
                    FileSystemBrowserTreeNode treeNode = (FileSystemBrowserTreeNode) object;
                    if ((treeNode == null) || (treeNode.isDisposed()))
                    {
                        this.targetSelection = null;
                        return;
                    }
                    if (treeNode.equals(this.targetSelection))
                    {
                        break;
                    }
                    if ((!treeNode.isLeaf()) && (!treeNode.isTreeStructureLoaded()))
                    {
                        selectPath(FileSystemBrowserUtils.createTreePath(treeNode));
                        treeNode.getChildren();
                        return;
                    }
                }
                this.targetSelection = null;
                selectPath(treePath);
            }
        }
    }

    /**
     * 获取选中的树节点
     * @return
     */
    public FileSystemBrowserTreeNode getSelectedNode()
    {
        TreePath selectionPath = getSelectionPath();
        return selectionPath == null ? null : (FileSystemBrowserTreeNode) selectionPath.getLastPathComponent();
    }

    /**
     * 获取选中的目标节点
     * @return
     */
    public FileSystemBrowserTreeNode getTargetSelection()
    {
        return this.targetSelection;
    }


    private void selectPath(final TreePath treePath)
    {
        new SimpleLaterInvocator()
        {
            protected void execute()
            {
                TreeUtil.selectPath(FileSystemBrowserTree.this, treePath, true);
            }
        }.start();
    }

    /**
     * 获取鼠标悬停节点的提示文字
     * @param event
     * @return
     */
    public String getToolTipText(MouseEvent event)
    {
        TreePath path = getClosestPathForLocation(event.getX(), event.getY());
        if (path != null)
        {
            Rectangle pathBounds = getPathBounds(path);
            if (pathBounds != null)
            {
                Point mouseLocation = GUIUtil.getRelativeMouseLocation(event.getComponent());
                if (pathBounds.contains(mouseLocation))
                {
                    Object object = path.getLastPathComponent();
                    if ((object instanceof ToolTipProvider))
                    {
                        ToolTipProvider toolTipProvider = (ToolTipProvider) object;
                        boolean ensureDataLoaded = DatabaseLoadMonitor.isEnsureDataLoaded();
                        try
                        {
                            DatabaseLoadMonitor.setEnsureDataLoaded(false);
                            return toolTipProvider.getToolTip();
                        } finally
                        {
                            DatabaseLoadMonitor.setEnsureDataLoaded(ensureDataLoaded);
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 上一个选中的节点
     */
    public void navigateBack()
    {
        FileSystemBrowserTreeNode treeNode = this.navigationHistory.previous();
        if (treeNode != null)
        {
            selectPathSilently(FileSystemBrowserUtils.createTreePath(treeNode));
        }
    }

    /**
     * 下个选中的节点
     */
    public void navigateForward()
    {
        FileSystemBrowserTreeNode treeNode = this.navigationHistory.next();
        if (treeNode != null)
        {
            selectPathSilently(FileSystemBrowserUtils.createTreePath(treeNode));
        }
    }

    public void selectPathSilently(TreePath treePath)
    {
        if (treePath != null)
        {
            this.listenersEnabled = false;
            this.selectionModel.setSelectionPath(treePath);
            TreeUtil.selectPath(this, treePath, true);
            this.listenersEnabled = true;
        }
    }

    private boolean listenersEnabled = true;

    /**
     * 展开所有树节点
     */
    public void expandAll()
    {
        FileSystemBrowserTreeNode root = getModel().getRoot();
        expand(root);
    }

    /**
     * 展开节点
     * @param treeNode
     */
    public void expand(FileSystemBrowserTreeNode treeNode)
    {
        if (treeNode.canExpand())
        {
            expandPath(FileSystemBrowserUtils.createTreePath(treeNode));
            for (int i = 0; i < treeNode.getChildCount(); i++)
            {
                FileSystemBrowserTreeNode childTreeNode = treeNode.getChildAt(i);
                expand(childTreeNode);
            }
        }
    }

    /**
     * 折叠所有的节点
     */
    public void collapseAll()
    {
        FileSystemBrowserTreeNode root = getModel().getRoot();
        collapse(root);
    }

    /**
     * 折叠节点
     * @param treeNode
     */
    public void collapse(FileSystemBrowserTreeNode treeNode)
    {
        if ((!treeNode.isLeaf()) && (treeNode.isTreeStructureLoaded()))
        {
            for (int i = 0; i < treeNode.getChildCount(); i++)
            {
                FileSystemBrowserTreeNode childTreeNode = treeNode.getChildAt(i);
                collapse(childTreeNode);
                collapsePath(FileSystemBrowserUtils.createTreePath(childTreeNode));
            }
        }
    }

    /**
     * 选中节点处理的事件进程
     * @param event
     * @param path
     * @param deliberate
     */
    private void processSelectEvent(InputEvent event, TreePath path, boolean deliberate)
    {
        if (path != null)
        {
            Object lastPathEntity = path.getLastPathComponent();
            if ((lastPathEntity instanceof FileSystemObject))
            {
                final FileSystemObject object = (FileSystemObject) lastPathEntity;
                FileSystemObjectProperties properties = object.getProperties();
                if (properties.is(FileSystemObjectProperty.EDITABLE))
                {
                    event.consume();
                } else if (properties.is(FileSystemObjectProperty.NAVIGABLE))
                {
                    event.consume();
                } else if (deliberate)
                {
                    new BackgroundTask(getProject(), "Loading Object Reference", false, false)
                    {
                        protected void execute(@NotNull ProgressIndicator progressIndicator)
                                throws InterruptedException
                        {
                            if (progressIndicator == null)
                            {
                                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"progressIndicator", "com/dci/intellij/dbn/browser/ui/FileSystemBrowserTree$3", "execute"}));
                            }
                            final FileSystemObject navigationObject = object.getDefaultNavigationObject();
                            if (navigationObject != null)
                            {
                                new SimpleLaterInvocator()
                                {
                                    protected void execute()
                                    {
                                        navigationObject.navigate(true);
                                    }
                                }.start();
                            }
                        }
                    }.start();
                }
            } else if ((lastPathEntity instanceof FileSystemObjectBundle))
            {
                FileSystemObjectBundle objectBundle = (FileSystemObjectBundle) lastPathEntity;

            }
        }
    }

    /**
     *TreeNode选中事件通知
     */
    private TreeSelectionListener treeSelectionListener = new TreeSelectionListener()
    {
        public void valueChanged(TreeSelectionEvent e)
        {
            if ((!isDisposed()) && (listenersEnabled))
            {
                Object object = e.getPath().getLastPathComponent();
                if ((object != null) && ((object instanceof FileSystemBrowserTreeNode)))
                {
                    FileSystemBrowserTreeNode treeNode = (FileSystemBrowserTreeNode) object;
                    if ((targetSelection == null) || (treeNode.equals(targetSelection)))
                    {
                        navigationHistory.add(treeNode);
                    }
                }
                (EventUtil.notify(FileSystemBrowserTree.this.getProject(), BrowserTreeEventListener.TOPIC)).selectionChanged();
            }
        }
    };
    /**
     * 树控件鼠标事件监听处理
     */
    private MouseListener mouseListener = new MouseAdapter()
    {
        /**
         * 鼠标单击事件
         * @param event
         */
        public void mouseClicked(MouseEvent event)
        {
            if (event.getButton() == 1)
            {
                FileSystemBrowserManager browserManager = FileSystemBrowserManager.getInstance(FileSystemBrowserTree.this.getProject());
                if (event.getClickCount() > 1)
                {
                    TreePath path = FileSystemBrowserTree.this.getPathForLocation(event.getX(), event.getY());
                    FileSystemBrowserTree.this.processSelectEvent(event, path, event.getClickCount() > 1);
                }
            }
        }

        /**
         *鼠标释放事件
         * @param event
         */
        public void mouseReleased(final MouseEvent event)
        {
            if (event.getButton() == 3)
            {
                TreePath path = FileSystemBrowserTree.this.getPathForLocation(event.getX(), event.getY());
                if (path != null)
                {
                    final FileSystemBrowserTreeNode lastPathEntity = (FileSystemBrowserTreeNode) path.getLastPathComponent();
                    if (lastPathEntity.isDisposed())
                    {
                        return;
                    }
                    //开启一个右键菜单判断操作线程
                    new ModalTask(lastPathEntity.getProject(), "Loading fsobject information", true)
                    {
                        //进行TreeNode的类型判断，并实例化菜单的Action
                        protected void execute(@NotNull ProgressIndicator progressIndicator)
                        {
                            ActionGroup actionGroup = null;
                            if ((lastPathEntity instanceof FileSystemObject))
                            {
                                //文件系统对象节点，右键菜单
                                FileSystemObject object = (FileSystemObject) lastPathEntity;
                                actionGroup = new ObjectActionGroup(object);
                            }
                            else if ((lastPathEntity instanceof FileSystemObjectBundle))
                            {
                                //文件系统对象集合节点
                                FileSystemObjectBundle objectsBundle = (FileSystemObjectBundle) lastPathEntity;
                                ConnectionHandler connectionHandler = objectsBundle.getConnectionHandler();
                                actionGroup = new ConnectionActionGroup(connectionHandler);
                            }
                            if ((actionGroup != null) && (!progressIndicator.isCanceled()))
                            {
                                //开启显示右键菜单操作线程
                                ActionPopupMenu actionPopupMenu = ActionManager.getInstance().createActionPopupMenu("", actionGroup);
                                popupMenu = actionPopupMenu.getComponent();
                                new SimpleLaterInvocator()
                                {
                                    protected void execute()
                                    {
                                        if (FileSystemBrowserTree.this.isShowing())
                                        {
                                            popupMenu.show(FileSystemBrowserTree.this,event.getX(),event.getY());
                                        }
                                    }
                                }.start();
                            } else
                            {
                                FileSystemBrowserTree.this.popupMenu = null;
                            }
                        }
                    }.start();
                }
            }
        }
    };

    /**
     * 树控件的键盘事件监听处理
     */
    private KeyListener keyListener = new KeyAdapter()
    {
        /**
         * 键盘健按下事件
         * @param e
         */
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == 10)
            {
                TreePath path = FileSystemBrowserTree.this.getSelectionPath();
                FileSystemBrowserTree.this.processSelectEvent(e, path, true);
            }
        }
    };

    /**
     * 资源释放
     */
    public void dispose()
    {
        super.dispose();
        this.targetSelection = null;
        this.treeSelectionListener = null;
        this.mouseListener = null;
        this.keyListener = null;
        this.treeModelListener = null;
        setModel(new SimpleBrowserTreeModel());
    }
}
