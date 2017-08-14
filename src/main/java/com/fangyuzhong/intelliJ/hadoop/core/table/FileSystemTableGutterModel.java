package com.fangyuzhong.intelliJ.hadoop.core.table;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class FileSystemTableGutterModel<T extends FileSystemTableWithGutterModel>
        extends DisposableBase
        implements ListModel, Disposable
{
    private T tableModel;
    private Set<ListDataListener> listeners = new HashSet();

    public FileSystemTableGutterModel(T tableModel)
    {
        this.tableModel = tableModel;
    }

    @NotNull
    public T getTableModel()
    {
        return FailsafeUtil.get(this.tableModel);
    }

    public int getSize()
    {
        return getTableModel().getRowCount();
    }

    public Object getElementAt(int index)
    {
        return Integer.valueOf(index + 1);
    }

    public void addListDataListener(ListDataListener l)
    {
        this.listeners.add(l);
    }

    public void removeListDataListener(ListDataListener l)
    {
        this.listeners.remove(l);
    }

    public void notifyListeners(ListDataEvent listDataEvent)
    {
        for (ListDataListener listDataListener : this.listeners)
        {
            listDataListener.contentsChanged(listDataEvent);
        }
    }

    public void dispose()
    {
        super.dispose();
        this.listeners.clear();
        this.tableModel = null;
    }
}