package com.fangyuzhong.intelliJ.hadoop.core.ui;

import com.intellij.openapi.actionSystem.CustomShortcutSet;
import com.intellij.openapi.actionSystem.KeyboardShortcut;
import com.intellij.openapi.actionSystem.Shortcut;
import com.intellij.openapi.actionSystem.ShortcutSet;
import com.intellij.openapi.keymap.Keymap;
import com.intellij.openapi.keymap.KeymapManager;
import com.intellij.openapi.keymap.KeymapUtil;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class KeyUtil
{
    public static boolean match(Shortcut[] shortcuts, KeyEvent e)
    {
        for (Shortcut shortcut : shortcuts)
        {
            if ((shortcut instanceof KeyboardShortcut))
            {
                KeyboardShortcut keyboardShortcut = (KeyboardShortcut) shortcut;
                KeyStroke shortkutKeyStroke = keyboardShortcut.getFirstKeyStroke();
                KeyStroke eventKeyStroke = KeyStroke.getKeyStrokeForEvent(e);
                if (shortkutKeyStroke.equals(eventKeyStroke))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static Shortcut[] getShortcuts(String actionId)
    {
        return KeymapManager.getInstance().getActiveKeymap().getShortcuts(actionId);
    }

    public static ShortcutSet createShortcutSet(int keyCode, int modifiers)
    {
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, modifiers);
        Shortcut shortcut = new KeyboardShortcut(keyStroke, null);
        return new CustomShortcutSet(new Shortcut[]{shortcut});
    }

    public static boolean isEmacsKeymap()
    {
        return KeymapUtil.isEmacsKeymap();
    }

    public static boolean isEmacsKeymap(@Nullable Keymap keymap)
    {
        return KeymapUtil.isEmacsKeymap(keymap);
    }
}
