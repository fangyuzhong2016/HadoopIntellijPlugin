package com.fangyuzhong.intelliJ.hadoop.core.table;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public  interface FileSystemTableWithGutterModel
        extends FileSystemTableModel
{
      ListModel getListModel();
}