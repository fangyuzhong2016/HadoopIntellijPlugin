package com.fangyuzhong.intelliJ.hadoop.core.options.ui;

import com.fangyuzhong.intelliJ.hadoop.core.options.Configuration;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemComboBox;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemBaseFormImpl;
import com.fangyuzhong.intelliJ.hadoop.core.ui.ValueSelectorListener;
import com.fangyuzhong.intelliJ.hadoop.core.ui.list.CheckBoxList;
import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.ProjectSupplier;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.DocumentAdapter;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * 定义配置编辑主窗体抽象类
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class ConfigurationEditorForm<E extends Configuration>
        extends FileSystemBaseFormImpl<ConfigurationEditorForm>
{
    public static final String DBN_REGISTERED = "DBN_REGISTERED";
    private ItemListener itemListener;
    private ActionListener actionListener;
    private DocumentListener documentListener;
    private TableModelListener tableModelListener;
    private E configuration;

    protected ConfigurationEditorForm(E configuration)
    {
        super(getProject(configuration));
        this.configuration = configuration;
    }

    protected static Project getProject(Configuration configuration)
    {
        if ((configuration instanceof ProjectSupplier))
        {
            ProjectSupplier projectSupplier = (ProjectSupplier) configuration;
            return projectSupplier.getProject();
        }
        return null;
    }

    public final E getConfiguration()
    {
        return this.configuration;
    }

    public abstract void applyFormChanges()
            throws ConfigurationException;

    public void applyFormChanges(E configuration)
            throws ConfigurationException
    {
        throw new UnsupportedOperationException("Not implemented by default");
    }

    public abstract void resetFormChanges();

    protected DocumentListener createDocumentListener()
    {
       return new DocumentAdapter()
        {
            protected void textChanged(DocumentEvent e)
            {
                ConfigurationEditorForm.this.getConfiguration().setModified(true);
            }
        };
    }

    protected ActionListener createActionListener()
    {
       return new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                ConfigurationEditorForm.this.getConfiguration().setModified(true);
            }
        };
    }

    protected ItemListener createItemListener()
    {
       return new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                ConfigurationEditorForm.this.getConfiguration().setModified(true);
            }
        };
    }

    protected TableModelListener createTableModelListener()
    {
       return new TableModelListener()
        {
            public void tableChanged(TableModelEvent e)
            {
                ConfigurationEditorForm.this.getConfiguration().setModified(true);
            }
        };
    }

    protected void registerComponents(JComponent... components)
    {
        for (JComponent component : components)
        {
            registerComponent(component);
        }
    }

    protected void registerComponent(JComponent component)
    {
        if (component.getClientProperty("DBN_REGISTERED") == null)
        {
            component.putClientProperty("DBN_REGISTERED", Boolean.valueOf(true));
            if ((component instanceof AbstractButton))
            {
                AbstractButton abstractButton = (AbstractButton) component;
                if (this.actionListener == null)
                {
                    this.actionListener = createActionListener();
                }
                abstractButton.addActionListener(this.actionListener);
            } else if ((component instanceof FileSystemComboBox))
            {
                FileSystemComboBox comboBox = (FileSystemComboBox) component;
                comboBox.addListener(new ValueSelectorListener()
                {
                    public void selectionChanged(Object oldValue, Object newValue)
                    {
                        if (!CommonUtil.safeEqual(oldValue, newValue))
                        {
                            ConfigurationEditorForm.this.getConfiguration().setModified(true);
                        }
                    }
                });
            } else if ((component instanceof CheckBoxList))
            {
                CheckBoxList checkBoxList = (CheckBoxList) component;
                if (this.actionListener == null)
                {
                    this.actionListener = createActionListener();
                }
                checkBoxList.addActionListener(this.actionListener);
            } else if ((component instanceof JTextField))
            {
                JTextField textField = (JTextField) component;
                if (this.documentListener == null)
                {
                    this.documentListener = createDocumentListener();
                }
                textField.getDocument().addDocumentListener(this.documentListener);
            } else if ((component instanceof JComboBox))
            {
                JComboBox comboBox = (JComboBox) component;
                if (this.itemListener == null)
                {
                    this.itemListener = createItemListener();
                }
                comboBox.addItemListener(this.itemListener);
            } else if ((component instanceof JTable))
            {
                JTable table = (JTable) component;
                if (this.tableModelListener == null)
                {
                    this.tableModelListener = createTableModelListener();
                }
                table.getModel().addTableModelListener(this.tableModelListener);
            } else
            {
                for (Component childComponent : component.getComponents())
                {
                    if ((childComponent instanceof JComponent))
                    {
                        registerComponent((JComponent) childComponent);
                    }
                }
            }
        }
    }

    public void focus()
    {
    }

    public void dispose()
    {
        super.dispose();
        this.configuration = null;
    }
}
