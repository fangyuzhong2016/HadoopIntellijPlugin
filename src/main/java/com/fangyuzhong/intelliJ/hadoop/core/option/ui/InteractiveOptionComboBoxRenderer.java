package com.fangyuzhong.intelliJ.hadoop.core.option.ui;

import com.fangyuzhong.intelliJ.hadoop.core.option.InteractiveOption;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class InteractiveOptionComboBoxRenderer
        extends ColoredListCellRenderer<InteractiveOption>
{
    public static final InteractiveOptionComboBoxRenderer INSTANCE = new InteractiveOptionComboBoxRenderer();

    protected void customizeCellRenderer(JList list, InteractiveOption value,
                                         int index, boolean selected, boolean hasFocus)
    {
        if (value != null)
        {
            setIcon(value.getIcon());
            append(value.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);
        }
    }
}
