package com.fangyuzhong.intelliJ.hadoop.core.option;

import com.fangyuzhong.intelliJ.hadoop.core.ui.Presentable;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public  interface InteractiveOption
        extends Presentable
{
      boolean isCancel();

      boolean isAsk();
}