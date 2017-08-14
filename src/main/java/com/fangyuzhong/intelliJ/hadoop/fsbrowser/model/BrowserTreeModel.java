package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tree.TreeEventType;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tree.TreeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserUtils;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.util.containers.HashSet;
import gnu.trove.THashSet;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 定义树节点模型TreeModel抽象类
 * Created by fangyuzhong on 17-7-16.
 */
public abstract class BrowserTreeModel
        implements TreeModel, Disposable
{
    private Set<TreeModelListener> treeModelListeners = new HashSet();
    private FileSystemBrowserTreeNode root;
    private boolean isDisposed = false;
    private final Set<LoadInProgressTreeNode> loadInProgressNodes = new THashSet();

    /**
     * 初始化 树节点模型
     * @param root
     */
    protected BrowserTreeModel(FileSystemBrowserTreeNode root)
    {
        this.root = root;
        EventUtil.subscribe(root.getProject(), this, BrowserTreeEventListener.TOPIC, this.browserTreeEventListener);
    }

    /**
     * 注册 Node 模型监听事件
     * @param listener
     */
    public void addTreeModelListener(TreeModelListener listener)
    {
        this.treeModelListeners.add(listener);
    }

    /**
     * 移除Node模型监听事件
     * @param listener
     */
    public void removeTreeModelListener(TreeModelListener listener)
    {
        this.treeModelListeners.remove(listener);
    }

    /**
     * 通知节点修改了，需要处理相关事件
     * @param treeNode 树节点
     * @param eventType 事件类型
     */
    public void notifyListeners(FileSystemBrowserTreeNode treeNode, TreeEventType eventType)
    {
        if ((FailsafeUtil.softCheck(this)) && (FailsafeUtil.softCheck(treeNode)))
        {
            TreePath treePath = FileSystemBrowserUtils.createTreePath(treeNode);
            //通知节点修改，按照事件类型，重新处理
            TreeUtil.notifyTreeModelListeners(this, this.treeModelListeners, treePath, eventType);
        }
    }

    /**
     * 获取当前的Project
     * @return
     */
    public Project getProject()
    {
        return getRoot().getProject();
    }

    /**
     * 检查是否包含指定的Node
     * @param paramBrowserTreeNode
     * @return
     */
    public abstract boolean contains(FileSystemBrowserTreeNode paramBrowserTreeNode);

    /**
     * 注册加载显示节点（使用定时器多线程加载）
     * @param node
     */
    private void registerLoadInProgressNode(LoadInProgressTreeNode node)
    {
        synchronized (this.loadInProgressNodes)
        {
            boolean startTimer = this.loadInProgressNodes.size() == 0;
            this.loadInProgressNodes.add(node);
            if (startTimer)
            {
                Timer reloader = new Timer("Hadoop Browser (load in progress reload timer)");
                reloader.schedule(new LoadInProgressRefreshTask(), 0L, 50L);
            }
        }
    }

    /**
     * 加载等待显示节点的线程类
     */
    private class LoadInProgressRefreshTask
            extends TimerTask
    {
        int iterations = 0;

        private LoadInProgressRefreshTask()
        {
        }

        public void run()
        {
            synchronized (loadInProgressNodes)
            {
                Iterator<LoadInProgressTreeNode> loadInProgressNodesIterator =loadInProgressNodes.iterator();
                while (loadInProgressNodesIterator.hasNext())
                {
                    LoadInProgressTreeNode loadInProgressTreeNode = loadInProgressNodesIterator.next();
                    try
                    {
                        if (loadInProgressTreeNode.isDisposed())
                        {
                            loadInProgressNodesIterator.remove();
                        } else
                        {
                           notifyListeners(loadInProgressTreeNode, TreeEventType.NODES_CHANGED);
                        }
                    } catch (ProcessCanceledException e)
                    {
                        loadInProgressNodesIterator.remove();
                    }
                }
                if (loadInProgressNodes.isEmpty())
                {
                    cancel();
                }
            }
            this.iterations += 1;
        }
    }

    /**
     * 获取根节点
     * @return
     */
    public FileSystemBrowserTreeNode getRoot()
    {
        return  FailsafeUtil.get(this.root);
    }

    /**
     * 获取父节点的子节点
     * @param parent
     * @param index
     * @return
     */
    public Object getChild(Object parent, int index)
    {
        FileSystemBrowserTreeNode treeChild = ((FileSystemBrowserTreeNode) parent).getChildAt(index);
        if ((treeChild instanceof LoadInProgressTreeNode))
        {
            registerLoadInProgressNode((LoadInProgressTreeNode) treeChild);
        }
        return treeChild;
    }

    /**
     * 获取父节点的子节点的数量
     * @param parent
     * @return
     */
    public int getChildCount(Object parent)
    {
        return ((FileSystemBrowserTreeNode) parent).getChildCount();
    }

    /**
     * 判断节点是否是叶子节点
     * @param node
     * @return
     */
    public boolean isLeaf(Object node)
    {
        return ((FileSystemBrowserTreeNode) node).isLeaf();
    }

    /**
     * 获取子节点所在的索引
     * @param parent
     * @param child
     * @return
     */
    public int getIndexOfChild(Object parent, Object child)
    {
        return ((FileSystemBrowserTreeNode) parent).getIndex((FileSystemBrowserTreeNode) child);
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
    }

    /**
     *
     */
    public void dispose()
    {
        if (!this.isDisposed)
        {
            this.isDisposed = true;
            this.treeModelListeners.clear();
            this.loadInProgressNodes.clear();
            this.root = null;
        }
    }

    /**
     * 树节点事件监听处理
     */
    private BrowserTreeEventListener browserTreeEventListener = new BrowserTreeEventAdapter()
    {
        public void nodeChanged(FileSystemBrowserTreeNode node, TreeEventType eventType)
        {
            if (contains(node))
            {
               notifyListeners(node, eventType);
            }
        }
    };
}
