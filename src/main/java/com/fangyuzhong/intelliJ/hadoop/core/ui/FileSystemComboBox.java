package com.fangyuzhong.intelliJ.hadoop.core.ui;

import java.util.List;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class FileSystemComboBox<T extends Presentable>
        extends ValueSelector<T>
{
    public FileSystemComboBox()
    {
        super(null, null, true, new ValueSelectorOption[0]);
    }

    public FileSystemComboBox(List<T> values, ValueSelectorOption... options)
    {
        super(null, null, values, null, true, options);
    }
}
