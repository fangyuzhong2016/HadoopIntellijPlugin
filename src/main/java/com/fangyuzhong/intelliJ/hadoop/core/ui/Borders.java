package com.fangyuzhong.intelliJ.hadoop.core.ui;

import com.fangyuzhong.intelliJ.hadoop.core.Colors;
import com.intellij.ui.JBColor;
import com.intellij.ui.border.CustomLineBorder;

import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public  interface Borders
{
    public static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    public static final Border EMPTY_BORDER = new EmptyBorder(EMPTY_INSETS);
    public static final Border TEXT_FIELD_BORDER = new EmptyBorder(0, 3, 0, 3);
    public static final Border COMPONENT_LINE_BORDER = new LineBorder(Colors.COMPONENT_BORDER_COLOR);
    public static final Border BOTTOM_LINE_BORDER = new CustomLineBorder(JBColor.border(), 0, 0, 1, 0);
}

