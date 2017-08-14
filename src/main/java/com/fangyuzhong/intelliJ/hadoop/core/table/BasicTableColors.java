package com.fangyuzhong.intelliJ.hadoop.core.table;

import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.util.ui.UIUtil;

import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class BasicTableColors
{
    private static boolean isDarkScheme;
    private static Color lineNumberColor;
    private static Color selectionForegroundColor;
    private static Color selectionBackgroundColor;
    private static Color caretRowColor;

    private static void init()
    {
        if ((lineNumberColor == null) || (isDarkScheme != UIUtil.isUnderDarcula()))
        {
            isDarkScheme = UIUtil.isUnderDarcula();

            EditorColorsScheme globalScheme = EditorColorsManager.getInstance().getGlobalScheme();
            lineNumberColor = globalScheme.getColor(EditorColors.LINE_NUMBERS_COLOR);
            selectionForegroundColor = globalScheme.getColor(EditorColors.SELECTION_FOREGROUND_COLOR);
            selectionBackgroundColor = globalScheme.getColor(EditorColors.SELECTION_BACKGROUND_COLOR);
            caretRowColor = globalScheme.getColor(EditorColors.CARET_ROW_COLOR);
        }
    }

    public static Color getLineNumberColor()
    {
        init();
        return lineNumberColor;
    }

    public static Color getSelectionForegroundColor()
    {
        init();
        return selectionForegroundColor;
    }

    public static Color getSelectionBackgroundColor()
    {
        init();
        return selectionBackgroundColor;
    }

    public static Color getCaretRowColor()
    {
        init();
        return caretRowColor;
    }
}