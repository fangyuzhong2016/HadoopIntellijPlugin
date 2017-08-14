package com.fangyuzhong.intelliJ.hadoop.core.ui.list;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public  interface Selectable<T>
        extends Comparable<T>
{
      Icon getIcon();

      String getName();

      String getError();

      boolean isSelected();

      boolean isMasterSelected();

      void setSelected(boolean paramBoolean);
}