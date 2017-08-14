package com.fangyuzhong.intelliJ.hadoop.fsobject.properties.ui;

import com.fangyuzhong.intelliJ.hadoop.core.table.FileSystemTable;
import com.fangyuzhong.intelliJ.hadoop.core.thread.BackgroundTask;
import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemBaseFormImpl;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemForm;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserManager;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.BrowserTreeEventAdapter;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.BrowserTreeEventListener;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.FileSystemBrowserTree;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectType;
import com.fangyuzhong.intelliJ.hadoop.fsobject.NamingUtil;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.UpdateLanguageListener;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.components.JBScrollPane;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class ObjectPropertiesForm
        extends FileSystemBaseFormImpl<FileSystemForm>
{
    private JPanel mainPanel;
    private JLabel objectLabel;
    private JLabel objectTypeLabel;
    private JTable objectPropertiesTable;
    private JScrollPane objectPropertiesScrollPane;
    private JPanel closeActionPanel;
    private FileSystemObject object;
    private BrowserTreeEventListener browserTreeEventListener;

    public JComponent getComponent()
    {
        return this.mainPanel;
    }

    public ObjectPropertiesForm(FileSystemForm parentForm)
    {
        super(parentForm);
        $$$setupUI$$$();
        this.browserTreeEventListener = new BrowserTreeEventAdapter()
        {
            public void selectionChanged()
            {
                FileSystemBrowserManager browserManager = FileSystemBrowserManager.getInstance(ObjectPropertiesForm.this.getProject());
                FileSystemBrowserTree activeBrowserTree = browserManager.getActiveBrowserTree();
                if (activeBrowserTree != null)
                {
                    FileSystemBrowserTreeNode treeNode = activeBrowserTree.getSelectedNode();
                    if ((treeNode instanceof FileSystemObject))
                    {
                        FileSystemObject object = (FileSystemObject) treeNode;
                        ObjectPropertiesForm.this.setObject(object);
                    }
                    else
                    {
                        objectPropertiesTable.removeAll();
                    }
                }

            }
        };
        this.objectPropertiesTable.setRowSelectionAllowed(false);
        this.objectPropertiesTable.setCellSelectionEnabled(true);
        this.objectPropertiesScrollPane.getViewport().setBackground(this.objectPropertiesTable.getBackground());
        objectTypeLabel.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.OBJECTTYPELABELTEXT));
        objectLabel.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.OBJECTLABELTEXT));
        EventUtil.subscribe(getProject(), this, BrowserTreeEventListener.TOPIC, this.browserTreeEventListener);
        EventUtil.subscribe(getProject(), this, UpdateLanguageListener.TOPIC, this.updateLanguageListener);
    }

    private UpdateLanguageListener updateLanguageListener = new UpdateLanguageListener()
    {
        @Override
        public void UpdateLanguage()
        {
            objectTypeLabel.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.OBJECTTYPELABELTEXT));
            objectLabel.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.OBJECTLABELTEXT));
        }
    };


    public FileSystemObject getObject()
    {
        return this.object;
    }

    public void setObject(final FileSystemObject object)
    {

        if (!object.equals(this.object))
        {
            this.object = object;

            new BackgroundTask(object.getProject(), "Rendering fsobject properties", true)
            {
                @Override
                public void execute(@NotNull ProgressIndicator progressIndicator)
                {
                    final ObjectPropertiesTableModel tableModel = new ObjectPropertiesTableModel(object.getPresentableProperties());
                    Disposer.register(ObjectPropertiesForm.this, tableModel);

                    new SimpleLaterInvocator()
                    {
                        public void execute()
                        {
                            objectLabel.setText(object.getName());
                            objectLabel.setIcon(object.getIcon());
                            String objectTypeName = LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.OBJECTTYPEDIRECTORY);
                            if (object.getObjectType() == FileSystemObjectType.FILE)
                            {
                                objectTypeName = LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.OBJECTTYPEFILE);
                            }
                            objectTypeLabel.setText(NamingUtil.capitalize(objectTypeName) + ":");

                            ObjectPropertiesTableModel oldTableModel = (ObjectPropertiesTableModel) objectPropertiesTable.getModel();
                            objectPropertiesTable.setModel(tableModel);
                            ((FileSystemTable) objectPropertiesTable).accommodateColumnsSize();
                            mainPanel.revalidate();
                            mainPanel.repaint();
                            Disposer.dispose(oldTableModel);
                        }
                    }.start();
                }
            }.start();
        }
    }

    public void cleanObjectPropertiesShow()
    {
        objectPropertiesTable.removeAll();
        objectPropertiesTable.updateUI();
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public void dispose()
    {
        super.dispose();
        this.object = null;
    }

    private void createUIComponents()
    {
        this.objectPropertiesTable = new ObjectPropertiesTable(null, new ObjectPropertiesTableModel());
        this.objectPropertiesTable.getTableHeader().setReorderingAllowed(false);
        Disposer.register(this, (Disposable) this.objectPropertiesTable);
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        createUIComponents();
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 8, 0, 0), -1, 0));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        objectTypeLabel = new JLabel();
        objectTypeLabel.setText("[object type]");
        panel1.add(objectTypeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        objectLabel = new JLabel();
        objectLabel.setText("[object name]");
        panel1.add(objectLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, 1, null, null, null, 0, false));
        closeActionPanel = new JPanel();
        closeActionPanel.setLayout(new BorderLayout(0, 0));
        panel1.add(closeActionPanel, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, 1, null, null, null, 0, false));
        objectPropertiesScrollPane = new JBScrollPane();
        mainPanel.add(objectPropertiesScrollPane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        objectPropertiesScrollPane.setViewportView(objectPropertiesTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}

