package com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposerUtil;
import com.fangyuzhong.intelliJ.hadoop.core.ui.GUIUtil;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemBaseFormImpl;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.option.BrowserDisplayMode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.option.listener.DisplayModeSettingsListener;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.fsobject.properties.ui.ObjectPropertiesForm;
import com.fangyuzhong.intelliJ.hadoop.options.general.GeneralProjectSettings;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.GuiUtils;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * 定义文件系统主界面
 * Created by fangyuzhong on 17-7-16.
 */
public class BrowserToolWindowForm
        extends FileSystemBaseFormImpl
{
    private JPanel mainPanel;
    private JPanel actionsPanel;
    private JPanel browserPanel;
    private JPanel closeActionPanel;
    private JPanel objectPropertiesPanel;
    private FileSystemBrowserForm browserForm;
    private BrowserDisplayMode displayMode = BrowserDisplayMode.TABBED;
    private ObjectPropertiesForm objectPropertiesForm;
    private DisplayModeSettingsListener displayModeSettingsListener;

    /**
     * 初始化
     * @param project
     */
    public BrowserToolWindowForm(Project project)
    {
        super(project);

        /*
        注册显示方式设置监听
         */
        this.displayModeSettingsListener = new DisplayModeSettingsListener()
        {
            public void displayModeChanged(BrowserDisplayMode displayMode)
            {
                if (BrowserToolWindowForm.this.getDisplayMode() != displayMode)
                {
                    BrowserToolWindowForm.this.setDisplayMode(displayMode);
                    BrowserToolWindowForm.this.rebuild();
                }
            }
        };
        //构建UI
        rebuild();
        //添加工具栏
        ActionToolbar actionToolbar = ActionUtil.createActionToolbar("", true,
                "HadoopNavigator.ActionGroup.Browser.Controls");
        actionToolbar.setTargetComponent(this.actionsPanel);
        this.actionsPanel.add(actionToolbar.getComponent());
        //添加文件对象属性显示UI
        this.objectPropertiesPanel.setVisible(true);
        this.objectPropertiesForm = new ObjectPropertiesForm(this);
        this.objectPropertiesPanel.add(this.objectPropertiesForm.getComponent());
        GuiUtils.replaceJSplitPaneWithIDEASplitter(this.mainPanel);
        GUIUtil.updateSplitterProportion(this.mainPanel, 0.7F);
        //通知显示方式设置修改
        EventUtil.subscribe(project, this, DisplayModeSettingsListener.TOPIC, this.displayModeSettingsListener);
    }

    /**
     * 构建界面UI
     */
    public void rebuild()
    {
        //获取文件树展示方式
        this.displayMode = GeneralProjectSettings.getInstance(getProject()).
                getBrowserSettings().getBrowserDisplayMode();
        this.browserPanel.removeAll();
        DisposerUtil.dispose(this.browserForm);
        //按照显示方式，创建Tree显示窗体
        this.browserForm = (this.displayMode == BrowserDisplayMode.SINGLE ?
                new SimpleBrowserForm(this) :
                this.displayMode == BrowserDisplayMode.TABBED ?
                        new TabbedBrowserForm(this) : null);
        this.browserPanel.add(this.browserForm.getComponent(), "Center");
        this.browserPanel.revalidate();
        this.browserPanel.repaint();
        Disposer.register(this, this.browserForm);
        if (objectPropertiesForm != null)
        {
            objectPropertiesForm.cleanObjectPropertiesShow();
        }
    }

    /**
     * 按照指定的连接ID重新构建UI
     * @param connectionId
     */
    public void rebuild(String connectionId)
    {
        if (((browserForm instanceof TabbedBrowserForm)) && (!browserForm.isDisposed()))
        {
            TabbedBrowserForm tabbedBrowserForm = (TabbedBrowserForm) browserForm;
            tabbedBrowserForm.refreshTabInfo(connectionId);
        }
    }

    public FileSystemBrowserTree getBrowserTree(ConnectionHandler connectionHandler)
    {
        if ((this.browserForm instanceof TabbedBrowserForm))
        {
            TabbedBrowserForm tabbedBrowserForm = (TabbedBrowserForm) this.browserForm;
            return tabbedBrowserForm.getBrowserTree(connectionHandler);
        }
        if ((this.browserForm instanceof SimpleBrowserForm))
        {
            return this.browserForm.getBrowserTree();
        }
        return null;
    }

    public void showObjectProperties()
    {
        FileSystemBrowserManager browserManager = FileSystemBrowserManager.getInstance(getProject());
        FileSystemBrowserTree activeBrowserTree = browserManager.getActiveBrowserTree();
        FileSystemBrowserTreeNode treeNode = activeBrowserTree == null ? null : activeBrowserTree.getSelectedNode();
        if ((treeNode instanceof FileSystemObject))
        {
            FileSystemObject object = (FileSystemObject) treeNode;
            this.objectPropertiesForm.setObject(object);
        }
        this.objectPropertiesPanel.setVisible(true);
    }

    public void hideObjectProperties()
    {
        this.objectPropertiesPanel.setVisible(false);
    }

    public BrowserDisplayMode getDisplayMode()
    {
        return this.displayMode;
    }

    public void setDisplayMode(BrowserDisplayMode displayMode)
    {
        this.displayMode = displayMode;
    }

    @Nullable
    public FileSystemBrowserTree getActiveBrowserTree()
    {
        return this.browserForm.getBrowserTree();
    }

    public FileSystemBrowserForm getBrowserForm()
    {
        return this.browserForm;
    }

    public JPanel getComponent()
    {
        return this.mainPanel;
    }

    public void dispose()
    {
        super.dispose();
        this.objectPropertiesForm = null;
        this.browserForm = null;
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
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        actionsPanel = new JPanel();
        actionsPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(actionsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerSize(1);
        splitPane1.setOrientation(0);
        panel1.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        browserPanel = new JPanel();
        browserPanel.setLayout(new BorderLayout(0, 0));
        splitPane1.setLeftComponent(browserPanel);
        objectPropertiesPanel = new JPanel();
        objectPropertiesPanel.setLayout(new BorderLayout(0, 0));
        splitPane1.setRightComponent(objectPropertiesPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}

