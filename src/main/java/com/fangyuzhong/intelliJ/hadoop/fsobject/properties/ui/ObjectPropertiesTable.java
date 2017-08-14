package com.fangyuzhong.intelliJ.hadoop.fsobject.properties.ui;

import com.fangyuzhong.intelliJ.hadoop.core.table.FileSystemTable;
import com.fangyuzhong.intelliJ.hadoop.core.ui.Borders;
import com.fangyuzhong.intelliJ.hadoop.core.ui.MouseUtil;
import com.fangyuzhong.intelliJ.hadoop.fsobject.properties.PresentableProperty;
import com.fangyuzhong.intelliJ.hadoop.core.table.FileSystemTableModel;
import com.intellij.openapi.project.Project;
import com.intellij.pom.Navigatable;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class ObjectPropertiesTable
        extends FileSystemTable
{
    public ObjectPropertiesTable(Project project, FileSystemTableModel tableModel)
    {
        super(project, tableModel, false);
        setDefaultRenderer(String.class, this.cellRenderer);
        setDefaultRenderer(PresentableProperty.class, this.cellRenderer);
        adjustRowHeight(3);

        //addMouseListener(this.mouseListener);
        //addKeyListener(this.keyListener);
    }

    private MouseListener mouseListener = new MouseAdapter()
    {
        public void mouseClicked(MouseEvent event)
        {
            if ((event.getButton() == 1) && (event.getClickCount() > 1))
            {
                ObjectPropertiesTable.this.navigateInBrowser();
                event.consume();
            }
            if (MouseUtil.isNavigationEvent(event))
            {
                ObjectPropertiesTable.this.navigateInBrowser();
                event.consume();
            }
        }
    };
    private KeyListener keyListener = new KeyAdapter()
    {
        public void keyTyped(KeyEvent e)
        {
            if (e.getKeyChar() == '\n')
            {
                ObjectPropertiesTable.this.navigateInBrowser();
            }
        }
    };

    private void navigateInBrowser()
    {
        int rowIndex = getSelectedRow();
        int columnIndex = getSelectedColumn();
        if (columnIndex == 1)
        {
            PresentableProperty presentableProperty = (PresentableProperty) getModel().getValueAt(rowIndex, 1);
            Navigatable navigatable = presentableProperty.getNavigatable();
            if (navigatable != null)
            {
                navigatable.navigate(true);
            }
        }
    }

    protected void processMouseMotionEvent(MouseEvent e)
    {
        if ((e.isControlDown()) && (e.getID() != 506) && (isNavigableCellAtMousePosition()))
        {
            setCursor(Cursor.getPredefinedCursor(12));
        } else
        {
            super.processMouseMotionEvent(e);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    private boolean isNavigableCellAtMousePosition()
    {
        Object value = getValueAtMouseLocation();
        if ((value instanceof PresentableProperty))
        {
            PresentableProperty property = (PresentableProperty) value;
            return property.getNavigatable() != null;
        }
        return false;
    }

    TableCellRenderer cellRenderer = new DefaultTableCellRenderer()
    {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            PresentableProperty property = (PresentableProperty) value;
            if (property != null)
            {
                if (column == 0)
                {
                    setIcon(null);
                    setText(property.getName());
                } else if (column == 1)
                {
                    setText(property.getValue());
                    setIcon(null);
                }
            }
            Dimension dimension = getSize();
            dimension.setSize(dimension.getWidth(), 30.0D);
            setSize(dimension);
            setBorder(Borders.TEXT_FIELD_BORDER);
            return component;
        }
    };
}

