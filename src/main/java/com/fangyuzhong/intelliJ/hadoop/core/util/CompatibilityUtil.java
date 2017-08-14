package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.util.Key;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class CompatibilityUtil
{
    private static final Key<PsiFile> HARD_REF_TO_PSI = Key.create("HARD_REFERENCE_TO_PSI");

    public static Color getEditorBackgroundColor(EditorEx editorEx)
    {
        return editorEx.getBackgroundColor();
    }

    public static ModuleType getModuleType(Module module)
    {
        return ModuleType.get(module);
    }

    public static void showSearchCompletionPopup(boolean byClickingToolbarButton, JComponent toolbarComponent, JBList list, String title, JTextField textField)
    {
        Utils.showCompletionPopup(byClickingToolbarButton ? toolbarComponent : null, list, title, textField, "");
    }

    public static void setSmallerFontForChildren(JComponent component)
    {
        Utils.setSmallerFontForChildren(component);
    }

    public static void setSmallerFont(JComponent component)
    {
        Utils.setSmallerFont(component);
    }

    public static boolean isUnderGTKLookAndFeel()
    {
        return UIUtil.isUnderGTKLookAndFeel();
    }

    public static boolean isUnderIntelliJLaF()
    {
        try
        {
            return UIUtil.isUnderIntelliJLaF();
        } catch (Error e)
        {
        }
        return UIManager.getLookAndFeel().getName().contains("IntelliJ");
    }
}
