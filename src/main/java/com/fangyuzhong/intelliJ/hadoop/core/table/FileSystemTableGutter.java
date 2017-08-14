package com.fangyuzhong.intelliJ.hadoop.core.table;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.intellij.openapi.editor.colors.EditorColorsListener;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public abstract class FileSystemTableGutter<T extends FileSystemTableWithGutter>
        extends JList
        implements Disposable, EditorColorsListener
{
    private boolean disposed;
    private T table;

    public FileSystemTableGutter(T table)
    {
        super(table.getModel().getListModel());
        this.table = table;
        int rowHeight = table.getRowHeight();
        if (rowHeight != 0)
        {
            setFixedCellHeight(rowHeight);
        }
        setBackground(UIUtil.getPanelBackground());

        setCellRenderer(createCellRenderer());

        EditorColorsManager.getInstance().addEditorColorsListener(this, this);
    }

    protected abstract ListCellRenderer createCellRenderer();

    public void globalSchemeChange(@Nullable EditorColorsScheme scheme)
    {
        setCellRenderer(createCellRenderer());
    }

    public ListModel getModel()
    {
        ListModel cachedModel = super.getModel();
        if (this.table == null)
        {
            return cachedModel;
        }
        ListModel listModel = this.table.getModel().getListModel();
        if (listModel != cachedModel)
        {
            setModel(listModel);
        }
        return listModel;
    }

    public T getTable()
    {
        return this.table;
    }

    public boolean isDisposed()
    {
        return this.disposed;
    }

    public void dispose()
    {
        this.disposed = true;
        this.table = null;
    }
}

