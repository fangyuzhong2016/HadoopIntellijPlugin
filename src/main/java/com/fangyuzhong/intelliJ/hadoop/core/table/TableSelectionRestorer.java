package com.fangyuzhong.intelliJ.hadoop.core.table;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public  interface TableSelectionRestorer
{
      void snapshot();

      void restore();

      boolean isRestoring();
}