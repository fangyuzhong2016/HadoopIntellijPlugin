package com.fangyuzhong.intelliJ.hadoop.core.table;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;
import com.fangyuzhong.intelliJ.hadoop.core.util.DisposableLazyValue;
import com.fangyuzhong.intelliJ.hadoop.core.util.LazyValue;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class FileSystemEditableTableModel
        extends DisposableBase
        implements FileSystemTableWithGutterModel
{
    private Set<TableModelListener> tableModelListeners = new HashSet();
    private LazyValue<FileSystemTableGutterModel> listModel = new DisposableLazyValue(this)
    {
        protected FileSystemTableGutterModel load()
        {
            return new FileSystemTableGutterModel(FileSystemEditableTableModel.this);
        }
    };

    public void addTableModelListener(TableModelListener listener)
    {
        this.tableModelListeners.add(listener);
    }

    public void removeTableModelListener(TableModelListener listener)
    {
        this.tableModelListeners.remove(listener);
    }

    public ListModel getListModel()
    {
        return (ListModel) this.listModel.get();
    }

    public abstract void insertRow(int paramInt);

    public abstract void removeRow(int paramInt);

    public void notifyListeners(int firstRowIndex, int lastRowIndex, int columnIndex)
    {
        TableModelEvent modelEvent = new TableModelEvent(this, firstRowIndex, lastRowIndex, columnIndex);
        for (TableModelListener listener : this.tableModelListeners)
        {
            listener.tableChanged(modelEvent);
        }
        if (this.listModel.isLoaded())
        {
            ListDataEvent listDataEvent = new ListDataEvent(this, 0, firstRowIndex, lastRowIndex);
            ((FileSystemTableGutterModel) this.listModel.get()).notifyListeners(listDataEvent);
        }
    }

    public void dispose()
    {
        super.dispose();
        this.tableModelListeners.clear();
    }
}
