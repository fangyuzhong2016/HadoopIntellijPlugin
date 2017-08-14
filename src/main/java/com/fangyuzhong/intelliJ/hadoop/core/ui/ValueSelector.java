package com.fangyuzhong.intelliJ.hadoop.core.ui;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleCallback;
import com.fangyuzhong.intelliJ.hadoop.core.util.ActionUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.CompatibilityUtil;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.openapi.ui.GraphicsConfig;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.Condition;
import com.intellij.ui.Gray;
import com.intellij.ui.JBColor;
import com.intellij.ui.RoundedLineBorder;
import com.intellij.util.IconUtil;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class ValueSelector<T extends Presentable>
        extends JPanel
{
    private Set<ValueSelectorListener<T>> listeners = new HashSet();
    private T selectedValue;
    private JLabel label;
    private JPanel innerPanel;
    private Icon icon;
    private String text;
    private boolean isComboBox;
    private boolean isEnabled = true;
    private boolean isFocused = false;
    private boolean isShowingPopup = false;
    private OptionBundle<ValueSelectorOption> options;
    private Border focusBorder;
    private Border defaultBorder;
    private Border insideBorder;
    private Border insideBorderFocused;
    private List<T> values;
    private PresentableFactory<T> valueFactory;

    public ValueSelector(@Nullable String text, @Nullable T preselectedValue, boolean isComboBox, ValueSelectorOption... options)
    {
        this(null, text, null, preselectedValue, isComboBox, options);
    }

    public ValueSelector(@Nullable Icon icon, @Nullable String text, @Nullable T preselectedValue, boolean isComboBox, ValueSelectorOption... options)
    {
        this(icon, text, null, preselectedValue, isComboBox, options);
    }

    public ValueSelector(@Nullable Icon icon, @Nullable String text, @Nullable List<T> values, @Nullable T preselectedValue, boolean isComboBox, ValueSelectorOption... options)
    {
        super(new BorderLayout());
        setOptions(options);
        this.values = values;
        if(text!=null)
        {
            text = CommonUtil.nvl(text, "");
        }
        else
        {
            text="";
        }
        this.icon = icon;
        this.text = text;
        this.isComboBox = isComboBox;

        setBorder(Borders.EMPTY_BORDER);
        if (isComboBox)
        {
            this.defaultBorder = new ValueSelectorBorder();
            this.focusBorder = this.defaultBorder;
        } else
        {
            this.insideBorder = new EmptyBorder(3, 5, 3, 5);
            this.insideBorderFocused = new EmptyBorder(2, 4, 2, 4);

            this.defaultBorder = this.insideBorder;
            this.focusBorder = new CompoundBorder(new RoundedLineBorder(new JBColor(Gray._190, Gray._55), 3), this.insideBorderFocused);
        }
        this.label = new JLabel(text, cropIcon(icon), 2);
        this.label.setCursor(Cursor.getPredefinedCursor(12));
        this.label.addMouseListener(this.mouseListener);

        this.innerPanel = new JPanel(new BorderLayout());
        this.innerPanel.setBorder(this.defaultBorder);
        this.innerPanel.add(this.label, "West");
        this.innerPanel.addMouseListener(this.mouseListener);
        this.innerPanel.setCursor(Cursor.getPredefinedCursor(12));
        add(this.innerPanel, "Center");
        if (isComboBox)
        {
            this.selectedValue = preselectedValue;
            if (this.selectedValue == null)
            {
                this.label.setIcon(cropIcon(icon));
                this.label.setText(text);
            } else
            {
                this.label.setIcon(cropIcon(this.selectedValue.getIcon()));
                this.label.setText(getName(this.selectedValue));
            }
            this.innerPanel.setBackground(UIUtil.getTextFieldBackground());
            this.innerPanel.add(new JLabel(Icons.COMMON_ARROW_DOWN), "East");

            this.innerPanel.setFocusable(true);
            this.innerPanel.addFocusListener(new FocusListener()
            {
                public void focusGained(FocusEvent e)
                {
                    ValueSelector.this.isFocused = true;
                    ValueSelector.this.revalidate();
                    ValueSelector.this.repaint();
                }

                public void focusLost(FocusEvent e)
                {
                    ValueSelector.this.isFocused = false;
                    ValueSelector.this.revalidate();
                    ValueSelector.this.repaint();
                }
            });
            this.innerPanel.addKeyListener(new KeyAdapter()
            {
                public void keyPressed(KeyEvent e)
                {
                    if (e.getKeyCode() == 38)
                    {
                        ValueSelector.this.selectPrevious();
                        e.consume();
                    } else if (e.getKeyCode() == 40)
                    {
                        ValueSelector.this.selectNext();
                        e.consume();
                    } else if (e.getKeyCode() == 10)
                    {
                        ValueSelector.this.showPopup();
                        e.consume();
                    }
                }
            });
        }
        adjustSize();
    }

    public void setOptions(ValueSelectorOption... options)
    {
        this.options = new OptionBundle(options);
    }

    private void adjustSize()
    {
        int minWidth = 0;
        FontMetrics fontMetrics = this.label.getFontMetrics(this.label.getFont());
        int height = fontMetrics.getHeight();
        for (T presentable : getAllPossibleValues())
        {
            String name = (String) CommonUtil.nvl(getName(presentable), "");
            int width = fontMetrics.stringWidth(name);
            if (presentable.getIcon() != null)
            {
                width += 16;
            }
            minWidth = Math.max(minWidth, width);
        }
        int width = fontMetrics.stringWidth(this.text);
        if (this.icon != null)
        {
            width += 16;
        }
        minWidth = Math.max(minWidth, width);
        this.label.setPreferredSize(new Dimension(minWidth + 10, height));
        this.label.setMinimumSize(new Dimension(minWidth + 10, height));
        this.innerPanel.setMaximumSize(new Dimension(-1, height + 2));
        this.innerPanel.setPreferredSize(new Dimension((int) this.innerPanel.getPreferredSize().getWidth(), height + 2));
    }

    public void setValueFactory(PresentableFactory<T> valueFactory)
    {
        this.valueFactory = valueFactory;
    }

    public void addListener(ValueSelectorListener<T> listener)
    {
        this.listeners.add(listener);
    }

    public void removeListener(ValueSelectorListener<T> listener)
    {
        this.listeners.remove(listener);
    }

    private static Icon cropIcon(Icon icon)
    {
        return icon == null ? null : IconUtil.cropIcon(icon, 16, 16);
    }

    public boolean isEnabled()
    {
        return this.isEnabled;
    }

    public void setEnabled(boolean isEnabled)
    {
        this.isEnabled = isEnabled;
        this.label.setCursor(isEnabled ? Cursor.getPredefinedCursor(12) : Cursor.getDefaultCursor());
        this.innerPanel.setCursor(isEnabled ? Cursor.getPredefinedCursor(12) : Cursor.getDefaultCursor());

        this.innerPanel.setBackground((this.isComboBox) && (isEnabled) ? UIUtil.getTextFieldBackground() : UIUtil.getPanelBackground());
        this.innerPanel.setFocusable(isEnabled);
        this.label.setForeground(isEnabled ? UIUtil.getTextFieldForeground() : UIUtil.getLabelDisabledForeground());
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }

    private MouseListener mouseListener = new MouseAdapter()
    {
        public void mouseEntered(MouseEvent e)
        {
            if ((!ValueSelector.this.isShowingPopup) && (!ValueSelector.this.isComboBox))
            {
                ValueSelector.this.innerPanel.setBorder(ValueSelector.this.focusBorder);
                ValueSelector.this.innerPanel.setBackground(new JBColor(Gray._210, Gray._75));

                ValueSelector.this.revalidate();
                ValueSelector.this.repaint();
            }
        }

        public void mouseExited(MouseEvent e)
        {
            if ((!ValueSelector.this.isShowingPopup) && (!ValueSelector.this.isComboBox))
            {
                ValueSelector.this.innerPanel.setBorder(ValueSelector.this.defaultBorder);

                ValueSelector.this.innerPanel.setBackground(ValueSelector.this.isComboBox ? UIUtil.getTextFieldBackground() : UIUtil.getPanelBackground());

                ValueSelector.this.revalidate();
                ValueSelector.this.repaint();
            }
        }

        public void mousePressed(MouseEvent e)
        {
            if (ValueSelector.this.getValues().size() == 0)
            {
                ValueSelector.this.selectValue(null);
            } else if ((ValueSelector.this.isEnabled) && (!ValueSelector.this.isShowingPopup))
            {
                ValueSelector.this.innerPanel.requestFocus();
                ValueSelector.this.showPopup();
            }
        }

        public void mouseMoved(MouseEvent e)
        {
            super.mouseMoved(e);
        }
    };

    private void showPopup()
    {
        this.isShowingPopup = true;
        this.innerPanel.setCursor(Cursor.getDefaultCursor());
        this.label.setCursor(Cursor.getDefaultCursor());
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        for (T value : getValues())
        {
            actionGroup.add(new SelectValueAction(value));
        }
        if (this.valueFactory != null)
        {
            actionGroup.add(ActionUtil.SEPARATOR);
            actionGroup.add(new AddValueAction());
        }
        ListPopup popup = JBPopupFactory.getInstance().createActionGroupPopup(null, actionGroup, DataManager.getInstance().getDataContext(this), false, false, false, new Runnable()
        {
            public void run()
            {
                ValueSelector.this.innerPanel.setBorder(ValueSelector.this.defaultBorder);
                ValueSelector.this.innerPanel.setBackground(ValueSelector.this.isComboBox ? UIUtil.getTextFieldBackground() : UIUtil.getPanelBackground());
                ValueSelector.this.innerPanel.setCursor(Cursor.getPredefinedCursor(12));
                ValueSelector.this.label.setCursor(Cursor.getPredefinedCursor(12));

                ValueSelector.this.isShowingPopup = false;
                ValueSelector.this.innerPanel.requestFocus();
                ValueSelector.this.revalidate();
                ValueSelector.this.repaint();
            }
        }, 10, new Condition<AnAction>()
        {
            public boolean value(AnAction anAction)
            {
                if ((anAction instanceof ValueSelector.SelectValueAction))
                {
                    ValueSelector<T>.SelectValueAction action = (ValueSelector.SelectValueAction) anAction;
                    return action.value.equals(ValueSelector.this.selectedValue);
                }
                return false;
            }
        });
        GUIUtil.showUnderneathOf(popup, this, 3, 200);
    }

    public void clearValues()
    {
        selectValue(null);
        this.values.clear();
    }

    public String getOptionDisplayName(T value)
    {
        return getName(value);
    }

    public class SelectValueAction
            extends DumbAwareAction
    {
        private T value;

        public SelectValueAction(T value)
        {
            super(null,null, ValueSelector.this.options.is(ValueSelectorOption.HIDE_ICON) ? null : value.getIcon());
            this.value = value;
        }

        public void actionPerformed(AnActionEvent e)
        {
            ValueSelector.this.selectValue(this.value);
            ValueSelector.this.innerPanel.requestFocus();
        }

        public void update(AnActionEvent e)
        {
            e.getPresentation().setVisible(ValueSelector.this.isVisible(this.value));
            e.getPresentation().setText(ValueSelector.this.getOptionDisplayName(this.value), false);
        }
    }

    private class AddValueAction
            extends DumbAwareAction
    {
        AddValueAction()
        {
            super(null,null, Icons.ACTION_ADD);
        }

        public void actionPerformed(AnActionEvent e)
        {
            ValueSelector.this.valueFactory.create(new SimpleCallback<T>()
            {
                public void start(@Nullable T inputValue)
                {
                    if (inputValue != null)
                    {
                        ValueSelector.this.addValue(inputValue);
                        ValueSelector.this.selectValue(inputValue);
                    }
                }
            });
        }

        public void update(AnActionEvent e)
        {
            e.getPresentation().setVisible(ValueSelector.this.valueFactory != null);
        }
    }

    @NotNull
    private String getName(T value)
    {
        if (value != null)
        {
            String description = value.getDescription();
            String name = value.getName();
            String
                    tmp72_66 = (name + " (" + description + ")");
            if (tmp72_66 == null)
            {
                throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/ui/ValueSelector", "getName"}));
            }
            return tmp72_66;
        }
        String tmp115_113 = "";
        if (tmp115_113 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/ui/ValueSelector", "getName"}));
        }
        return tmp115_113;
    }

    public boolean isVisible(T value)
    {
        return true;
    }

    @Nullable
    public T getSelectedValue()
    {
        return this.selectedValue;
    }

    public void setSelectedValue(@Nullable T value)
    {
        selectValue(value);
    }

    public final List<T> getValues()
    {
        if (this.values == null)
        {
            this.values = loadValues();
        }
        return this.values;
    }

    protected List<T> loadValues()
    {
        return new ArrayList();
    }

    protected List<T> getAllPossibleValues()
    {
        return getValues();
    }

    public void setValues(T... values)
    {
        setValues(Arrays.asList(values));
    }

    public void setValues(List<T> values)
    {
        this.values = values;
        adjustSize();
    }

    private void addValue(T value)
    {
        this.values.add(value);
        adjustSize();
    }

    public void addValues(Collection<T> value)
    {
        this.values.addAll(value);
        adjustSize();
    }

    public void resetValues()
    {
        this.values = null;
    }

    private void selectValue(T value)
    {
        T oldValue = this.selectedValue;
        if (value != null)
        {
            value = this.values.isEmpty() ? null : this.values.contains(value) ? value : this.values.get(0);
        }
        if ((!CommonUtil.safeEqual(oldValue, value)) || ((this.values.isEmpty()) && (value == null)))
        {
            if (this.isComboBox)
            {
                this.selectedValue = value;
                if (this.selectedValue == null)
                {
                    this.label.setIcon(this.options.is(ValueSelectorOption.HIDE_ICON) ? null : cropIcon(this.icon));
                    this.label.setText(this.text);
                } else
                {
                    this.label.setIcon(this.options.is(ValueSelectorOption.HIDE_ICON) ? null : cropIcon(this.selectedValue.getIcon()));
                    this.label.setText(getName(this.selectedValue));
                }
            }
            for (ValueSelectorListener<T> listener : this.listeners)
            {
                listener.selectionChanged(oldValue, value);
            }
        }
    }

    void selectNext()
    {
        if ((this.isComboBox) && (this.selectedValue != null))
        {
            List<T> values = getValues();
            int index = values.indexOf(this.selectedValue);
            if (index < values.size() - 1)
            {
                T nextValue = values.get(index + 1);
                selectValue(nextValue);
            }
        }
    }

    void selectPrevious()
    {
        if ((this.isComboBox) && (this.selectedValue != null))
        {
            List<T> values = getValues();
            int index = values.indexOf(this.selectedValue);
            if (index > 0)
            {
                T previousValue =  values.get(index - 1);
                selectValue(previousValue);
            }
        }
    }

    private class ValueSelectorBorder
            implements Border, UIResource
    {
        ValueSelector<T> valueSelector;

        ValueSelectorBorder()
        {
            this.valueSelector = valueSelector;
        }

        public Insets getBorderInsets(Component c)
        {
            return new InsetsUIResource(4, 6, 4, 6);
        }

        public boolean isBorderOpaque()
        {
            return false;
        }

        public void paintBorder(Component c, Graphics g2, int x, int y, int width, int height)
        {
            Graphics2D g = (Graphics2D) g2;
            GraphicsConfig config = new GraphicsConfig(g);
            g.translate(x, y);
            if ((UIUtil.isUnderDarcula()) || (CompatibilityUtil.isUnderIntelliJLaF()))
            {
                if ((ValueSelector.this.isFocused) || (ValueSelector.this.isShowingPopup))
                {
                    GUIUtil.paintFocusRing(g, new Rectangle(2, 2, width - 4, height - 4));
                } else
                {
                    boolean editable = this.valueSelector.isEnabled;
                    g.setColor(getBorderColor((c.isEnabled()) && (editable)));
                    g.drawRect(1, 1, width - 2, height - 2);
                    g.setColor(UIUtil.getPanelBackground());
                    g.drawRect(0, 0, width, height);
                }
            } else
            {
                Border textFieldBorder = UIUtil.getTextFieldBorder();
                if ((textFieldBorder instanceof LineBorder))
                {
                    LineBorder lineBorder = (LineBorder) textFieldBorder;
                    g.setColor(lineBorder.getLineColor());
                } else
                {
                    g.setColor(UIUtil.getBorderColor());
                }
                g.drawRect(1, 1, width - 3, height - 3);
                g.setColor(UIUtil.getPanelBackground());
                g.drawRect(0, 0, width - 1, height - 1);
            }
            g.translate(-x, -y);
            config.restore();
        }

        private Color getBorderColor(boolean enabled)
        {
            if (UIUtil.isUnderDarcula())
            {
                return enabled ? Gray._100 : Gray._83;
            }
            return Gray._150;
        }
    }

    public abstract class NewValueCallable
            implements Callable<T>
    {
        private String actionName;

        public NewValueCallable(String actionName)
        {
            this.actionName = actionName;
        }

        public String getActionName()
        {
            return this.actionName;
        }
    }
}
