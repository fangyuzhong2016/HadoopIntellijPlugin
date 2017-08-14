package com.fangyuzhong.intelliJ.hadoop.core.table;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class IndexTableGutter<T extends FileSystemTableWithGutter>
        extends FileSystemTableGutter<T>
{
    public IndexTableGutter(T table)
    {
        super(table);
    }

    protected ListCellRenderer createCellRenderer()
    {
        return new IndexTableGutterCellRenderer();
    }
}