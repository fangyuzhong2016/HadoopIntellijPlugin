package com.fangyuzhong.intelliJ.hadoop.core.ui.list;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class CheckBoxList<T extends Selectable>
        extends JList
{
    private boolean mutable;
    private MouseAdapter mouseAdapter;

    public CheckBoxList(List<T> elements)
    {
        this(elements, false);
    }

    public CheckBoxList(List<T> elements, boolean mutable)
    {
        this.mutable = mutable;
        setSelectionMode(mutable ? 2 : 0);

        setCellRenderer(new CellRenderer());
        setBackground(UIUtil.getTextFieldBackground());

        this.mouseAdapter = new MouseAdapter()
        {
            public void mousePressed(MouseEvent e)
            {
                if ((CheckBoxList.this.isEnabled()) && (e.getButton() == 1))
                {
                    int index = CheckBoxList.this.locationToIndex(e.getPoint());
                    if (index != -1)
                    {
                        CheckBoxList.Entry entry = (CheckBoxList.Entry) CheckBoxList.this.getModel().getElementAt(index);
                        if ((!CheckBoxList.this.mutable) || (e.getX() < 20) || (e.getClickCount() == 2))
                        {
                            entry.switchSelection();
                        }
                    }
                }
            }
        };
        DefaultListModel model = new DefaultListModel();
        for (T element : elements)
        {
            Entry<T> entry = new Entry(element);
            model.addElement(entry);
        }
        setModel(model);

        addMouseListener(this.mouseAdapter);

        addKeyListener(new KeyAdapter()
        {
            public void keyTyped(KeyEvent e)
            {
                if (e.getKeyChar() == ' ')
                {
                    int[] indices = CheckBoxList.this.getSelectedIndices();
                    for (int index : indices)
                    {
                        if (index >= 0)
                        {
                            CheckBoxList.Entry entry = (CheckBoxList.Entry) CheckBoxList.this.getModel().getElementAt(index);
                            entry.switchSelection();
                        }
                    }
                }
            }
        });
    }

    public boolean isSelected(T presentable)
    {
        for (int i = 0; i < getModel().getSize(); i++)
        {
            CheckBoxList<T>.Entry<T> entry = (Entry) getModel().getElementAt(i);
            if (entry.getPresentable().equals(presentable))
            {
                return entry.isSelected();
            }
        }
        return false;
    }

    public void selectAll()
    {
        for (int i = 0; i < getModel().getSize(); i++)
        {
            CheckBoxList<T>.Entry<T> entry = (Entry) getModel().getElementAt(i);
            entry.checkBox.setSelected(true);
        }
        revalidate();
        repaint();
    }

    private class CellRenderer
            implements ListCellRenderer
    {
        private CellRenderer()
        {
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
        {
            CheckBoxList.Entry entry = (CheckBoxList.Entry) value;
            Selectable presentable = entry.presentable;
            entry.checkBox.setEnabled(presentable.isMasterSelected());
            if (CheckBoxList.this.mutable)
            {
                Color foreground = entry.isSelected() ? UIUtil.getListForeground() : isSelected ? UIUtil.getListSelectionForeground() : UIUtil.getMenuItemDisabledForeground();
                Color background = isSelected ? UIUtil.getListSelectionBackground() : UIUtil.getTextFieldBackground();
                entry.textPanel.setBackground(background);
                entry.checkBox.setBackground(background);
                entry.label.setForeground(foreground);
            } else
            {
                Color background = list.isEnabled() ? UIUtil.getTextFieldBackground() : UIUtil.getComboBoxDisabledBackground();

                entry.textPanel.setBackground(background);
                entry.checkBox.setBackground(background);
                entry.setBorder(new LineBorder(background));
                entry.label.setForeground((presentable.isMasterSelected()) && (entry.isSelected()) ? UIUtil.getListForeground() : UIUtil.getMenuItemDisabledForeground());
            }
            return entry;
        }
    }

    public void sortElements(final Comparator<T> comparator)
    {
        List<CheckBoxList<T>.Entry<T>> entries = new ArrayList();
        ListModel model = getModel();
        for (int i = 0; i < model.getSize(); i++)
        {
            CheckBoxList<T>.Entry<T> entry = (Entry) model.getElementAt(i);
            entries.add(entry);
        }
        if (comparator == null)
        {
            Collections.sort(entries);
        } else
        {
            Collections.sort(entries, new Comparator<Entry<T>>()
            {
                public int compare(CheckBoxList<T>.Entry<T> o1, CheckBoxList<T>.Entry<T> o2)
                {
                    return comparator.compare(o1.presentable, o2.presentable);
                }
            });
        }
        DefaultListModel newModel = new DefaultListModel();
        for (CheckBoxList<T>.Entry<T> entry : entries)
        {
            newModel.addElement(entry);
        }
        setModel(newModel);
    }

    public boolean applyChanges()
    {
        boolean changed = false;
        ListModel model = getModel();
        for (int i = 0; i < model.getSize(); i++)
        {
            Entry entry = (Entry) model.getElementAt(i);
            changed = (entry.updatePresentable()) || (changed);
        }
        return changed;
    }

    public void addActionListener(ActionListener actionListener)
    {
        DefaultListModel model = (DefaultListModel) getModel();
        for (Object o : model.toArray())
        {
            Entry entry = (Entry) o;
            entry.checkBox.addActionListener(actionListener);
        }
    }

    public void removeActionListener(ActionListener actionListener)
    {
        DefaultListModel model = (DefaultListModel) getModel();
        for (Object o : model.toArray())
        {
            Entry entry = (Entry) o;
            entry.checkBox.removeActionListener(actionListener);
        }
    }

    public T getElementAt(int index)
    {
        CheckBoxList<T>.Entry<T> entry = (Entry) getModel().getElementAt(index);
        return entry.presentable;
    }

    private class Entry<T extends Selectable> extends JPanel implements Comparable<Entry<T>>
    {
        private JPanel textPanel;
        private JCheckBox checkBox;
        private JLabel label;
        private JLabel errorLabel;
        private T presentable;

        @Override
        public synchronized void addMouseListener(MouseListener l)
        {
            label.addMouseListener(l);
        }

        private Entry(T presentable)
        {
            super(new BorderLayout());
            setBackground(UIUtil.getListBackground());
            this.presentable = presentable;
            checkBox = new JCheckBox("", presentable.isSelected());
            checkBox.setBackground(UIUtil.getListBackground());

            label = new JLabel(presentable.getName(), presentable.getIcon(), SwingConstants.LEFT);
            //label.setForeground(error != null ? Color.RED : GUIUtil.getListForeground());
            errorLabel = new JLabel();
            errorLabel.setForeground(JBColor.RED);
            add(checkBox, BorderLayout.WEST);

            textPanel = new JPanel(new BorderLayout());
            textPanel.add(label, BorderLayout.WEST);
            textPanel.add(errorLabel, BorderLayout.CENTER);
            textPanel.setBackground(UIUtil.getListBackground());
            textPanel.setBorder(new EmptyBorder(0, 8, 0, 0));
            add(textPanel, BorderLayout.CENTER);
        }

        private boolean updatePresentable()
        {
            boolean changed = presentable.isSelected() != checkBox.isSelected();
            presentable.setSelected(checkBox.isSelected());
            return changed;
        }

        public T getPresentable()
        {
            return presentable;
        }

        public boolean isSelected()
        {
            return checkBox.isSelected();
        }

        private void switchSelection()
        {
            //if (checkBox.isEnabled()){
            checkBox.setSelected(!checkBox.isSelected());

            CheckBoxList.this.revalidate();
            CheckBoxList.this.repaint();
            for (ActionListener actionListener : checkBox.getActionListeners())
            {
                actionListener.actionPerformed(new ActionEvent(checkBox, 0, "selectionChanged"));
            }
            //}
        }

        @Override
        public int compareTo(@NotNull Entry<T> o)
        {
            return presentable.compareTo(o.presentable);
        }
    }
}
