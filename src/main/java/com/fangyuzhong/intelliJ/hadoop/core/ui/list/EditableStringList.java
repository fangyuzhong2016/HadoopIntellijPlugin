package com.fangyuzhong.intelliJ.hadoop.core.ui.list;

import com.fangyuzhong.intelliJ.hadoop.core.ui.Borders;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.fangyuzhong.intelliJ.hadoop.core.table.FileSystemEditableTable;
import com.fangyuzhong.intelliJ.hadoop.core.table.FileSystemEditableTableModel;
import com.fangyuzhong.intelliJ.hadoop.core.table.FileSystemTableGutter;
import com.fangyuzhong.intelliJ.hadoop.core.table.IndexTableGutter;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class EditableStringList
        extends FileSystemEditableTable<EditableStringList.EditableListModel>
{
    private boolean sorted;
    private boolean indexed;

    public EditableStringList(boolean sorted, boolean indexed)
    {
        this(null, new ArrayList(), sorted, indexed);
    }

    public EditableStringList(Project project, List<String> elements, boolean sorted, boolean indexed)
    {
        super(project, new EditableListModel(elements, sorted), false);
        setTableHeader(null);
        this.sorted = sorted;
        this.indexed = indexed;
        if (indexed)
        {
            getColumnModel().getColumn(0).setPreferredWidth(20);
            addFocusListener(new FocusAdapter()
            {
                public void focusGained(FocusEvent e)
                {
                    EditableStringList.this.accommodateColumnsSize();
                }
            });
        }
        addKeyListener(this.keyListener);
    }

    public FileSystemTableGutter createTableGutter()
    {
        return this.indexed ? new IndexTableGutter(this) : null;
    }

    public Component prepareEditor(final TableCellEditor editor, int rowIndex, int columnIndex)
    {
        JTextField component = (JTextField) super.prepareEditor(editor, rowIndex, columnIndex);
        component.setBorder(Borders.TEXT_FIELD_BORDER);
        component.addFocusListener(new FocusAdapter()
        {
            public void focusLost(FocusEvent e)
            {
                if (e.getOppositeComponent() != EditableStringList.this)
                {
                    editor.stopCellEditing();
                }
            }
        });
        component.addKeyListener(this.keyListener);
        return component;
    }

    private KeyAdapter keyListener = new KeyAdapter()
    {
        public void keyPressed(KeyEvent e)
        {
            if (!e.isConsumed())
            {
                int selectedRow = EditableStringList.this.getSelectedRow();
                int keyCode = e.getKeyCode();
                EditableStringList.EditableListModel model = (EditableStringList.EditableListModel) EditableStringList.this.getModel();
                if (keyCode == 40)
                {
                    if (selectedRow == model.getRowCount() - 1)
                    {
                        e.consume();
                        EditableStringList.this.insertRow();
                    }
                } else if ((keyCode == 10) && (e.getModifiers() == 0))
                {
                    e.consume();
                    EditableStringList.this.insertRow();
                } else if ((keyCode == 8) || (keyCode == 127))
                {
                    Object source = e.getSource();
                    String value = source == EditableStringList.this ? (String) model.getValueAt(selectedRow, 0) : ((JTextField) source).getText();
                    if (StringUtil.isEmpty(value))
                    {
                        e.consume();
                        EditableStringList.this.removeRow();
                    }
                }
            }
        }
    };

    public Component getEditorComponent()
    {
        return super.getEditorComponent();
    }

    public List<String> getStringValues()
    {
        return ((EditableListModel) getModel()).getData();
    }

    public void setStringValues(Collection<String> stringValues)
    {
        setModel(new EditableListModel(stringValues, this.sorted));
    }

    public static class EditableListModel
            extends FileSystemEditableTableModel
    {
        private List<String> data;

        public EditableListModel(Collection<String> data, boolean sorted)
        {
            this.data = new ArrayList(data);
            if (sorted)
            {
                Collections.sort(this.data);
            }
        }

        public List<String> getData()
        {
            return this.data;
        }

        public int getRowCount()
        {
            return this.data.size();
        }

        public int getColumnCount()
        {
            return 1;
        }

        public String getColumnName(int columnIndex)
        {
            return "DATA";
        }

        public Class<?> getColumnClass(int columnIndex)
        {
            return String.class;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex)
        {
            return true;
        }

        public Object getValueAt(int rowIndex, int columnIndex)
        {
            return this.data.get(rowIndex);
        }

        public void setValueAt(Object value, int rowIndex, int columnIndex)
        {
            boolean change = true;
            if (rowIndex < this.data.size())
            {
                String currentValue = (String) this.data.get(rowIndex);
                if (currentValue.equals(value))
                {
                    change = false;
                }
            }
            if (change)
            {
                this.data.set(rowIndex, (String) value);
                notifyListeners(rowIndex, rowIndex, columnIndex);
            }
        }

        public void insertRow(int rowIndex)
        {
            this.data.add(rowIndex, "");
            notifyListeners(rowIndex, this.data.size() + 1, -1);
        }

        public void removeRow(int rowIndex)
        {
            if ((rowIndex > -1) && (rowIndex < this.data.size()))
            {
                this.data.remove(rowIndex);
                notifyListeners(rowIndex, this.data.size() + 1, -1);
            }
        }
    }
}

