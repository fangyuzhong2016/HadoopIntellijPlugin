package com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposerUtil;
import com.fangyuzhong.intelliJ.hadoop.core.ui.tab.TabbedPane;
import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBList;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * 以Tab列表的方式显示多个HDFS连接
 * Created by fangyuzhong on 17-7-16.
 */
public class TabbedBrowserForm
        extends FileSystemBrowserForm
{
    private TabbedPane connectionTabs;
    private JPanel mainPanel;

    /**
     * 初始化
     * @param parentComponent
     */
    public TabbedBrowserForm(BrowserToolWindowForm parentComponent)
    {
        super(parentComponent);
        this.connectionTabs = new TabbedPane(this);
        initTabs();
        this.connectionTabs.addListener(new TabsListener()
                                        {
                                            public void selectionChanged(TabInfo oldSelection, TabInfo newSelection)
                                            {
                                            }

                                            public void beforeSelectionChanged(TabInfo oldSelection, TabInfo newSelection)
                                            {
                                            }

                                            public void tabRemoved(TabInfo tabInfo)
                                            {
                                            }

                                            public void tabsMoved()
                                            {
                                            }
                                        }

        );
        Disposer.register(this, this.connectionTabs);
    }

    /**
     * 初始化Tab 列表
     */
    private void initTabs()
    {
        Project project = getProject();
        TabbedPane oldConnectionTabs = this.connectionTabs;
        this.connectionTabs = new TabbedPane(this);
        ConnectionManager connectionManager = ConnectionManager.getInstance(project);
        //获取HDFS连接集合，遍历连接集合，读取每个连接，创建一个Tab页面
        ConnectionBundle connectionBundle = connectionManager.getConnectionBundle();
        for (ConnectionHandler connectionHandler : connectionBundle.getConnectionHandlers())
        {
            //根据每个连接，实例化一个 SimpleBrowserForm，放到一个新的Tab页上
            SimpleBrowserForm browserForm = new SimpleBrowserForm(this, connectionHandler);
            JComponent component = browserForm.getComponent();
            TabInfo tabInfo = new TabInfo(component);
            tabInfo.setText(CommonUtil.nvl(connectionHandler.getName(), "[unnamed fsconnection]"));
            tabInfo.setObject(browserForm);
            this.connectionTabs.addTab(tabInfo);
        }
        //添加connectiontab 到主面板控件mainPanel
        if (this.connectionTabs.getTabCount() == 0)
        {
            this.mainPanel.removeAll();
            this.mainPanel.add(new JBList(new ArrayList()), "Center");
        }
        else if (this.mainPanel.getComponentCount() > 0)
        {
            Component component = this.mainPanel.getComponent(0);
            if (component != this.connectionTabs)
            {
                this.mainPanel.removeAll();
                this.mainPanel.add(this.connectionTabs, "Center");
            }
        }
        else
        {
            this.mainPanel.add(this.connectionTabs, "Center");
        }
        DisposerUtil.dispose(oldConnectionTabs);
    }

    /**
     * 根据连接处理对象，获取到其所在的Tab页上的窗体
     * @param connectionHandler
     * @return
     */
    @Nullable
    private SimpleBrowserForm getBrowserForm(ConnectionHandler connectionHandler)
    {
        for (TabInfo tabInfo : this.connectionTabs.getTabs())
        {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            if (browserForm.getConnectionHandler() == connectionHandler)
            {
                return browserForm;
            }
        }
        return null;
    }

    public JComponent getComponent()
    {
        return this.mainPanel;
    }

    /**
     * 获取当前活动的Tree
     * @return
     */
    @Nullable
    public FileSystemBrowserTree getBrowserTree()
    {
        return getActiveBrowserTree();
    }

    /**
     * 获取指定连接的Tree
     * @param connectionHandler
     * @return
     */
    @Nullable
    public FileSystemBrowserTree getBrowserTree(ConnectionHandler connectionHandler)
    {
        SimpleBrowserForm browserForm = getBrowserForm(connectionHandler);
        return browserForm == null ? null : browserForm.getBrowserTree();
    }

    /**
     * 获取当前激活的Tree
     * @return
     */
    @Nullable
    public FileSystemBrowserTree getActiveBrowserTree()
    {
        TabInfo tabInfo = this.connectionTabs.getSelectedInfo();
        if (tabInfo != null)
        {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            return browserForm.getBrowserTree();
        }
        return null;
    }

    /**
     * 设置选中元素
     * @param treeNode
     * @param focus
     * @param scroll
     */
    public void selectElement(FileSystemBrowserTreeNode treeNode, boolean focus, boolean scroll)
    {
        ConnectionHandler connectionHandler = treeNode.getConnectionHandler();
        SimpleBrowserForm browserForm = getBrowserForm(connectionHandler);
        if (browserForm != null)
        {
            this.connectionTabs.select(browserForm.getComponent(), focus);
            if (scroll)
            {
                browserForm.selectElement(treeNode, focus, scroll);
            }
        }
    }

    /**
     * 构建树
     */
    public void rebuildTree()
    {
        for (TabInfo tabInfo : this.connectionTabs.getTabs())
        {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            browserForm.rebuildTree();
        }
    }

    public void dispose()
    {
        super.dispose();
    }

    /**
     * 根据连接ID刷新Tab页信息
     * @param connectionId
     */
    public void refreshTabInfo(String connectionId)
    {
        for (TabInfo tabInfo : this.connectionTabs.getTabs())
        {
            SimpleBrowserForm browserForm = (SimpleBrowserForm) tabInfo.getObject();
            ConnectionHandler connectionHandler = browserForm.getConnectionHandler();
            if (connectionHandler.getId().equals(connectionId))
            {
                tabInfo.setText(connectionHandler.getName());
                break;
            }
        }
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont)
    {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null)
        {
            resultName = currentFont.getName();
        } else
        {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1'))
            {
                resultName = fontName;
            } else
            {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}
