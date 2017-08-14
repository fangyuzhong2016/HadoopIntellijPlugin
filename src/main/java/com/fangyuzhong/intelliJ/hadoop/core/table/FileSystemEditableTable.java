package com.fangyuzhong.intelliJ.hadoop.core.table;

import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.fangyuzhong.intelliJ.hadoop.core.ui.Borders;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredTableCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.TableUtil;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class FileSystemEditableTable<T extends FileSystemEditableTableModel>
        extends FileSystemTableWithGutter<T>
{
    public static final LineBorder SELECTION_BORDER = new LineBorder(UIUtil.getTableBackground());

    public FileSystemEditableTable(Project project, T model, boolean showHeader)
    {
        super(project, model, showHeader);
        setSelectionMode(1);
        getSelectionModel().addListSelectionListener(this.selectionListener);
        setSelectionBackground(UIUtil.getTableBackground());
        setSelectionForeground(UIUtil.getTableForeground());
        setCellSelectionEnabled(true);
        putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);

        setDefaultRenderer(String.class, new ColoredTableCellRenderer()
        {
            protected void customizeCellRenderer(JTable table, Object value, boolean selected, boolean hasFocus, int row, int column)
            {
                acquireState(table, false, false, row, column);
                Color background = table.getBackground();
                Color foreground = table.getForeground();
                SimpleTextAttributes attributes = SimpleTextAttributes.SIMPLE_CELL_ATTRIBUTES;
                if ((selected) && (!table.isEditing()))
                {
                    background = UIUtil.getListSelectionBackground();
                    foreground = UIUtil.getListSelectionForeground();
                    attributes = SimpleTextAttributes.SELECTED_SIMPLE_CELL_ATTRIBUTES;
                }
                setBorder(new LineBorder(background, 2));
                setBackground(background);
                setForeground(foreground);
                append(value == null ? "" : (String) value, attributes);
            }
        });
    }

    private ListSelectionListener selectionListener = new ListSelectionListener()
    {
        public void valueChanged(ListSelectionEvent e)
        {
            if ((!e.getValueIsAdjusting()) && (FileSystemEditableTable.this.getSelectedRowCount() == 1))
            {
                FileSystemEditableTable.this.startCellEditing();
            }
        }
    };

    public void columnSelectionChanged(ListSelectionEvent e)
    {
        super.columnSelectionChanged(e);
        JTableHeader tableHeader = getTableHeader();
        if ((tableHeader != null) && (tableHeader.getDraggedColumn() == null) &&
                (!e.getValueIsAdjusting()))
        {
            startCellEditing();
        }
    }

    private void startCellEditing()
    {
        if (((FileSystemEditableTableModel) getModel()).getRowCount() > 0)
        {
            int selectedRow = getSelectedRow();
            int selectedColumn = getSelectedColumn();
            if ((selectedRow > -1) && (selectedColumn > -1))
            {
                TableCellEditor cellEditor = getCellEditor();
                if (cellEditor == null)
                {
                    editCellAt(selectedRow, selectedColumn);
                }
            }
        }
    }

    public void editingStopped(ChangeEvent e)
    {
        super.editingStopped(e);
        T model = (T) getModel();
        model.notifyListeners(0, model.getRowCount(), 0);
    }

    public Component prepareEditor(TableCellEditor editor, int rowIndex, int columnIndex)
    {
        final Component component = super.prepareEditor(editor, rowIndex, columnIndex);
        if ((component instanceof JTextField))
        {
            final JTextField textField = (JTextField) component;
            textField.setBorder(Borders.TEXT_FIELD_BORDER);

            new SimpleLaterInvocator()
            {
                protected void execute()
                {
                    component.requestFocus();
                    textField.selectAll();
                }
            }.start();
        }
        return component;
    }

    public void insertRow()
    {
        stopCellEditing();
        int rowIndex = getSelectedRow();
        T model = (T) getModel();
        rowIndex = model.getRowCount() == 0 ? 0 : rowIndex + 1;
        model.insertRow(rowIndex);
        resizeAndRepaint();
        getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
    }

    public void removeRow()
    {
        stopCellEditing();
        int selectedRow = getSelectedRow();
        T model = (T) getModel();
        model.removeRow(selectedRow);
        resizeAndRepaint();
        if ((model.getRowCount() == selectedRow) && (selectedRow > 0))
        {
            getSelectionModel().setSelectionInterval(selectedRow - 1, selectedRow - 1);
        }
    }

    public void moveRowUp()
    {
        int selectedRow = getSelectedRow();
        int selectedColumn = getSelectedColumn();

        TableUtil.moveSelectedItemsUp(this);
        selectCell(selectedRow - 1, selectedColumn);
    }

    public void moveRowDown()
    {
        int selectedRow = getSelectedRow();
        int selectedColumn = getSelectedColumn();

        TableUtil.moveSelectedItemsDown(this);
        selectCell(selectedRow + 1, selectedColumn);
    }
}
