package com.fangyuzhong.intelliJ.hadoop.core.ui;

import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.ComboBoxAction;

import javax.swing.*;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class FileSystemComboBoxAction
        extends ComboBoxAction
{
    public JComponent createCustomComponent(Presentation presentation)
    {
        JPanel panel = new JPanel(new GridBagLayout());
        ComboBoxAction.ComboBoxButton button = new ComboBoxAction.ComboBoxButton(presentation);
        GridBagConstraints constraints = new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(3, 3, 3, 3), 0, 0);

        panel.add(button, constraints);
        panel.setFocusable(false);
        return panel;
    }
}

