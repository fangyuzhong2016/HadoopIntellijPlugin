package com.fangyuzhong.intelliJ.hadoop.core.table;

import com.fangyuzhong.intelliJ.hadoop.core.ui.Borders;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.colors.EditorFontType;
import com.intellij.ui.border.CustomLineBorder;
import com.intellij.util.ui.UIUtil;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class IndexTableGutterCellRenderer
        extends JPanel
        implements ListCellRenderer
{
    static EditorColorsScheme getGlobalScheme()
    {
        return EditorColorsManager.getInstance().getGlobalScheme();
    }

    private static final Border BORDER = new CompoundBorder(new CustomLineBorder(UIUtil.getPanelBackground(), 0, 0, 1, 1), Borders.TEXT_FIELD_BORDER);
    private JLabel textLabel;

    public IndexTableGutterCellRenderer()
    {
        setBackground(UIUtil.getPanelBackground());
        setBorder(BORDER);
        setLayout(new BorderLayout());
        this.textLabel = new JLabel();
        this.textLabel.setForeground(BasicTableColors.getLineNumberColor());
        this.textLabel.setFont(EditorColorsManager.getInstance().getGlobalScheme().getFont(EditorFontType.PLAIN));
        add(this.textLabel, "East");
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
    {
        FileSystemTableGutter tableGutter = (FileSystemTableGutter) list;
        this.textLabel.setText(Integer.toString(index));
        FileSystemTable table = tableGutter.getTable();
        boolean isCaretRow = (table.getCellSelectionEnabled()) && (table.getSelectedRow() == index) && (table.getSelectedRowCount() == 1);

        setBackground(isCaretRow ? BasicTableColors.getCaretRowColor() : isSelected ? BasicTableColors.getSelectionBackgroundColor() : UIUtil.getPanelBackground());

        this.textLabel.setForeground(isSelected ? BasicTableColors.getSelectionForegroundColor() : BasicTableColors.getLineNumberColor());
        return this;
    }
}
