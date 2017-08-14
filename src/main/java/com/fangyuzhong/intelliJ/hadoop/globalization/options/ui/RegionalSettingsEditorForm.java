package com.fangyuzhong.intelliJ.hadoop.globalization.options.ui;

import com.fangyuzhong.intelliJ.hadoop.core.options.ui.ConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageSettingsListener;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.fangyuzhong.intelliJ.hadoop.globalization.options.LocaleOption;
import com.fangyuzhong.intelliJ.hadoop.globalization.options.RegionalSettings;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.ui.DocumentAdapter;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.fangyuzhong.intelliJ.hadoop.core.ui.GUIUtil.updateBorderTitleForeground;

/**
 * Created by fangyuzhong on 17-8-1.
 */
public class RegionalSettingsEditorForm extends ConfigurationEditorForm<RegionalSettings>
{
    private JPanel mainPanel;
    private JComboBox localeComboBox;
    private JLabel localeLabel;
    boolean isUpdating = false;

    public RegionalSettingsEditorForm(RegionalSettings regionalSettings)
    {
        super(regionalSettings);
        updateUiLanguage();
        updateBorderTitleForeground(mainPanel);
        for (LocaleOption var : LocaleOption.ALL)
        {
            localeComboBox.addItem(var);
        }
        resetFormChanges();
        registerComponent(mainPanel);
    }

    @Override
    protected ItemListener createItemListener()
    {
        return new ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                getConfiguration().setModified(true);
            }
        };
    }

    protected ActionListener createActionListener()
    {
        return new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                getConfiguration().setModified(true);
            }
        };
    }

    @Override
    protected DocumentListener createDocumentListener()
    {
        return new DocumentAdapter()
        {
            @Override
            protected void textChanged(DocumentEvent e)
            {
                getConfiguration().setModified(true);
            }
        };
    }

    public Locale getSelectedLocale()
    {
        LocaleOption localeOption = (LocaleOption) localeComboBox.getSelectedItem();
        return localeOption == null ? null : localeOption.getLocale();
    }

    public void setSelectedLocale(Locale locale)
    {
        int indexSelect = 0;
        for (int i = 0; i < localeComboBox.getItemCount(); i++)
        {
            String strLanguage = ((LocaleOption) localeComboBox.getItemAt(i)).getLocale().getLanguage();
            if (locale == null) return;
            if (locale.getLanguage() == strLanguage)
            {
                indexSelect = i;
                break;
            }
        }
        localeComboBox.setSelectedIndex(indexSelect);
    }


    public JPanel getComponent()
    {
        return mainPanel;
    }

    public void applyFormChanges() throws ConfigurationException
    {
        RegionalSettings regionalSettings = getConfiguration();
        Locale oldLocale = regionalSettings.getLocale();
        Locale locale = getSelectedLocale();
        regionalSettings.setLocale(locale);
        boolean languageChanged = true;
        if (locale != null)
        {
            languageChanged = !locale.equals(oldLocale);
        }
        if (languageChanged)
        {
            EventUtil.notify(this.getProject(), LanguageSettingsListener.TOPIC).LanguageChanged(locale);
            updateUiLanguage();
        }
    }


    private void updateUiLanguage()
    {
        ResourceBundle resourceBundle = LocaleLanguageManager.getInstance().getResourceBundle();
        if (resourceBundle == null) return;
        TitledBorder border = (TitledBorder) mainPanel.getBorder();
        border.setTitle(resourceBundle.getString(LanguageKeyWord.SETTINGLANGUAGEPANELTILE));
        localeLabel.setText(resourceBundle.getString(LanguageKeyWord.SETTINGSELECTLANGUAGE));
    }

    public void resetFormChanges()
    {
        RegionalSettings regionalSettings = getConfiguration();
        setSelectedLocale(regionalSettings.getLocale());
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont)
    {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null)
        {
            resultName = currentFont.getName();
        } else
        {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1'))
            {
                resultName = fontName;
            } else
            {
                resultName = currentFont.getName();
            }
        }
        return new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$()
    {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(2, 4, new Insets(4, 4, 4, 4), -1, -1));
        mainPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Regional Settings"));
        localeLabel = new JLabel();
        localeLabel.setText("Locale:");
        localeLabel.setDisplayedMnemonic('L');
        localeLabel.setDisplayedMnemonicIndex(0);
        mainPanel.add(localeLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(1, 1, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        localeComboBox = new JComboBox();
        mainPanel.add(localeComboBox, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}

