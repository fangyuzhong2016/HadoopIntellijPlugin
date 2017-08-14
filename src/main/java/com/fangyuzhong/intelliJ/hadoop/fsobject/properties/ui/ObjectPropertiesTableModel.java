package com.fangyuzhong.intelliJ.hadoop.fsobject.properties.ui;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;
import com.fangyuzhong.intelliJ.hadoop.fsobject.properties.PresentableProperty;
import com.fangyuzhong.intelliJ.hadoop.core.table.FileSystemTableModel;

import javax.swing.event.TableModelListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class ObjectPropertiesTableModel
        extends DisposableBase
        implements FileSystemTableModel
{
    private List<PresentableProperty> presentableProperties = new ArrayList();

    public ObjectPropertiesTableModel()
    {
    }

    public ObjectPropertiesTableModel(List<PresentableProperty> presentableProperties)
    {
        this.presentableProperties = presentableProperties;
    }

    public int getRowCount()
    {
        return this.presentableProperties.size();
    }

    public int getColumnCount()
    {
        return 2;
    }

    public String getColumnName(int columnIndex)
    {
        return columnIndex == 1 ? "Value" : columnIndex == 0 ? "Property" : null;
    }

    public Class<?> getColumnClass(int columnIndex)
    {
        return String.class;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    public Object getValueAt(int rowIndex, int columnIndex)
    {
        return this.presentableProperties.get(rowIndex);
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
    }

    public void addTableModelListener(TableModelListener l)
    {
    }

    public void removeTableModelListener(TableModelListener l)
    {
    }

    public void dispose()
    {
        super.dispose();
        this.presentableProperties.clear();
    }
}
