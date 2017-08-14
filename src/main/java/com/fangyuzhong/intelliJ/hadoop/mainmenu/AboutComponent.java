package com.fangyuzhong.intelliJ.hadoop.mainmenu;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.ui.Borders;
import com.fangyuzhong.intelliJ.hadoop.core.ui.FileSystemBaseFormImpl;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.ide.BrowserUtil;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.ide.plugins.PluginManager;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ComponentPopupBuilder;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

/**
 * Created by fangyuzhong on 17-7-31.
 */
public class AboutComponent
        extends FileSystemBaseFormImpl
{
    private JPanel mainPanel;
    private JLabel splashLabel;
    private JLabel donateLabel;
    private JLabel downloadPageLinkLabel;
    private JLabel supportPageLinkLabel;
    private JLabel requestTrackerPageLinkLabel;
    private JLabel buildLabel;
    private JPanel linksPanel;
    private JLabel fileSystemNavigatorLabel;
    private JLabel version30Label;
    private JLabel supportedFileSystemLabel;
    private JLabel downloadPageLabel;
    private JLabel supportPageLabel;
    private JLabel requestTrackerLabel;

    private final String PLUGINID = "com.fangyuzhong.intelliJ.hadoop";

    public AboutComponent(Project project)
    {
        super(project);
        Cursor handCursor = Cursor.getPredefinedCursor(12);
        updateLanguage();
        this.splashLabel.setIcon(Icons.DATABASE_NAVIGATOR);
        this.splashLabel.setText("");
        this.linksPanel.setBorder(Borders.BOTTOM_LINE_BORDER);

        this.donateLabel.setIcon(Icons.DONATE_DISABLED);
        this.donateLabel.setText("");
        this.donateLabel.setCursor(handCursor);
        this.donateLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                BrowserUtil.browse("http://www.fangyuzhong.com");
            }

            public void mouseEntered(MouseEvent e)
            {
                AboutComponent.this.donateLabel.setIcon(Icons.DONATE);
            }

            public void mouseExited(MouseEvent e)
            {
                AboutComponent.this.donateLabel.setIcon(Icons.DONATE_DISABLED);
            }
        });
        this.downloadPageLinkLabel.setForeground(CodeInsightColors.HYPERLINK_ATTRIBUTES.getDefaultAttributes().getForegroundColor());
        this.downloadPageLinkLabel.setCursor(handCursor);
        this.downloadPageLinkLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                BrowserUtil.browse("https://github.com/fangyuzhong2016");
            }
        });
        this.supportPageLinkLabel.setForeground(CodeInsightColors.HYPERLINK_ATTRIBUTES.getDefaultAttributes().getForegroundColor());
        this.supportPageLinkLabel.setCursor(handCursor);
        this.supportPageLinkLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                BrowserUtil.browse("http://www.fangyuzhong.com");
            }
        });
        this.requestTrackerPageLinkLabel.setForeground(CodeInsightColors.HYPERLINK_ATTRIBUTES.getDefaultAttributes().getForegroundColor());
        this.requestTrackerPageLinkLabel.setCursor(handCursor);
        this.requestTrackerPageLinkLabel.addMouseListener(new MouseAdapter()
        {
            public void mouseClicked(MouseEvent e)
            {
                BrowserUtil.browse("http://www.fangyuzhong.com");
            }
        });
        IdeaPluginDescriptor ideaPluginDescriptor = PluginManager.getPlugin(PluginId.getId(PLUGINID));
        String version = ideaPluginDescriptor.getVersion();
        this.version30Label.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.VERSION30LABEL) + version.substring(0, 3));
        this.buildLabel.setText(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.BUILDLABEL) + ": " + version.substring(4, 8));
    }

    private void updateLanguage()
    {
        ResourceBundle resourceBundle = LocaleLanguageManager.getInstance().getResourceBundle();
        if (resourceBundle == null) return;
        downloadPageLabel.setText(resourceBundle.getString(LanguageKeyWord.DOWNLOADPAGELABEL));
        supportPageLabel.setText(resourceBundle.getString(LanguageKeyWord.SUPPORTPAGELABEL));
        requestTrackerLabel.setText(resourceBundle.getString(LanguageKeyWord.REQUESTTRACKERLABEL));
        buildLabel.setText(resourceBundle.getString(LanguageKeyWord.BUILDLABEL));
        fileSystemNavigatorLabel.setText(resourceBundle.getString(LanguageKeyWord.FILESYSTEMNAVIGATORLABEL));
        version30Label.setText(resourceBundle.getString(LanguageKeyWord.VERSION30LABEL));
        supportedFileSystemLabel.setText(resourceBundle.getString(LanguageKeyWord.SUPPORTEDFILESYSTEMLABEL));
    }

    public JComponent getComponent()
    {
        return this.mainPanel;
    }

    public void showPopup(Project project)
    {
        ComponentPopupBuilder popupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(this.mainPanel, null);
        JBPopup popup = popupBuilder.createPopup();
        popup.showCenteredInCurrentWindow(project);
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
        mainPanel.setLayout(new GridLayoutManager(1, 1, new Insets(4, 4, 4, 4), -1, -1));
        mainPanel.setBackground(SystemColor.controlShadow);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(6, 4, new Insets(8, 8, 8, 8), 4, 0));
        mainPanel.add(panel1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 2, new Insets(8, 0, 0, 0), -1, -1));
        panel1.add(panel2, new GridConstraints(5, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        donateLabel = new JLabel();
        donateLabel.setText("[donate]");
        panel2.add(donateLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel2.add(spacer1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        version30Label = new JLabel();
        version30Label.setText("Version: 3.0");
        panel1.add(version30Label, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buildLabel = new JLabel();
        buildLabel.setText("Build: [build]");
        panel1.add(buildLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(2, 1, new Insets(0, 8, 0, 8), -1, -1));
        panel1.add(panel3, new GridConstraints(0, 0, 3, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        splashLabel = new JLabel();
        splashLabel.setText("[splash]");
        panel3.add(splashLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        fileSystemNavigatorLabel = new JLabel();
        Font fileSystemNavigatorLabelFont = this.$$$getFont$$$(null, Font.BOLD, 14, fileSystemNavigatorLabel.getFont());
        if (fileSystemNavigatorLabelFont != null) fileSystemNavigatorLabel.setFont(fileSystemNavigatorLabelFont);
        fileSystemNavigatorLabel.setText("Hadoop FileSystem");
        panel1.add(fileSystemNavigatorLabel, new GridConstraints(0, 1, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer3 = new Spacer();
        panel1.add(spacer3, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        linksPanel = new JPanel();
        linksPanel.setLayout(new GridLayoutManager(4, 3, new Insets(8, 0, 0, 0), 2, 0));
        panel1.add(linksPanel, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        downloadPageLabel = new JLabel();
        Font downloadPageLabelFont = this.$$$getFont$$$(null, -1, 10, downloadPageLabel.getFont());
        if (downloadPageLabelFont != null) downloadPageLabel.setFont(downloadPageLabelFont);
        downloadPageLabel.setText("Download Page:");
        linksPanel.add(downloadPageLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer4 = new Spacer();
        linksPanel.add(spacer4, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        supportPageLabel = new JLabel();
        Font supportPageLabelFont = this.$$$getFont$$$(null, -1, 10, supportPageLabel.getFont());
        if (supportPageLabelFont != null) supportPageLabel.setFont(supportPageLabelFont);
        supportPageLabel.setText("Support Page:");
        linksPanel.add(supportPageLabel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        downloadPageLinkLabel = new JLabel();
        Font downloadPageLinkLabelFont = this.$$$getFont$$$(null, -1, 10, downloadPageLinkLabel.getFont());
        if (downloadPageLinkLabelFont != null) downloadPageLinkLabel.setFont(downloadPageLinkLabelFont);
        downloadPageLinkLabel.setText("https://github.com/fangyuzhong2016");
        linksPanel.add(downloadPageLinkLabel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        supportPageLinkLabel = new JLabel();
        Font supportPageLinkLabelFont = this.$$$getFont$$$(null, -1, 10, supportPageLinkLabel.getFont());
        if (supportPageLinkLabelFont != null) supportPageLinkLabel.setFont(supportPageLinkLabelFont);
        supportPageLinkLabel.setText("http://www.fangyuzhong.com/");
        linksPanel.add(supportPageLinkLabel, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        requestTrackerLabel = new JLabel();
        Font requestTrackerLabelFont = this.$$$getFont$$$(null, -1, 10, requestTrackerLabel.getFont());
        if (requestTrackerLabelFont != null) requestTrackerLabel.setFont(requestTrackerLabelFont);
        requestTrackerLabel.setText("Request Tracker:");
        linksPanel.add(requestTrackerLabel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        requestTrackerPageLinkLabel = new JLabel();
        Font requestTrackerPageLinkLabelFont = this.$$$getFont$$$(null, -1, 10, requestTrackerPageLinkLabel.getFont());
        if (requestTrackerPageLinkLabelFont != null)
            requestTrackerPageLinkLabel.setFont(requestTrackerPageLinkLabelFont);
        requestTrackerPageLinkLabel.setText("http://www.fangyuzhong.com/      906328924@qq.com");
        linksPanel.add(requestTrackerPageLinkLabel, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer5 = new Spacer();
        linksPanel.add(spacer5, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, new Dimension(-1, 10), new Dimension(-1, 10), null, 0, false));
        supportedFileSystemLabel = new JLabel();
        supportedFileSystemLabel.setText("Supported FileSystem:");
        panel1.add(supportedFileSystemLabel, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        Font label1Font = this.$$$getFont$$$(null, Font.BOLD, -1, label1.getFont());
        if (label1Font != null) label1.setFont(label1Font);
        label1.setText("Hadoop(HDFS)");
        panel1.add(label1, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}
