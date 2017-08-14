package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.colors.ColorKey;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.SimpleTextAttributes;

import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class TextAttributesUtil
{
    public static SimpleTextAttributes getSimpleTextAttributes(TextAttributesKey textAttributesKey)
    {
        EditorColorsManager colorManager = EditorColorsManager.getInstance();
        TextAttributes textAttributes = colorManager.getGlobalScheme().getAttributes(textAttributesKey);
        if (textAttributes == null)
        {
            textAttributes = HighlighterColors.TEXT.getDefaultAttributes();
        }
        return new SimpleTextAttributes(textAttributes.getBackgroundColor(), textAttributes.getForegroundColor(), textAttributes.getEffectColor(), textAttributes.getFontType());
    }

    public static Color getColor(ColorKey colorKey)
    {
        EditorColorsManager colorManager = EditorColorsManager.getInstance();
        return colorManager.getGlobalScheme().getColor(colorKey);
    }
}

