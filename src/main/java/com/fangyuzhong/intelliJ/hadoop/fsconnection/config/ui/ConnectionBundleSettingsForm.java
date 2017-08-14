package com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.fangyuzhong.intelliJ.hadoop.core.options.SettingsChangeNotifier;
import com.fangyuzhong.intelliJ.hadoop.core.options.ui.ConfigurationEditorForm;
import com.fangyuzhong.intelliJ.hadoop.core.ui.GUIUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.ClipboardUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.EventUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandlerImpl;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.*;
import com.fangyuzhong.intelliJ.hadoop.fsobject.NamingUtil;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.*;
import com.intellij.ui.components.JBList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.UIUtil;
import org.apache.commons.lang.ArrayUtils;
import org.apache.xmlbeans.impl.common.ReaderInputStream;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class ConnectionBundleSettingsForm extends
        ConfigurationEditorForm<ConnectionBundleSettings>
        implements ListSelectionListener
{
    private static final Logger LOGGER = LoggerFactory.createLogger();
    private static final String BLANK_PANEL_ID = "BLANK_PANEL";

    private JPanel mainPanel;
    private JPanel connectionSetupPanel;
    private JPanel connectionListPanel;
    private JPanel actionsPanel;
    private JList connectionsList;

    private String currentPanelId;


    private Map<String, ConnectionSettingsForm> cachedForms = new HashMap<String, ConnectionSettingsForm>();

    public JList getList()
    {
        return connectionsList;
    }

    public ConnectionBundleSettingsForm(ConnectionBundleSettings configuration)
    {
        super(configuration);
        ConnectionBundle connectionBundle = configuration.getConnectionBundle();
        connectionsList = new JBList(new ConnectionListModel(connectionBundle));
        connectionsList.addListSelectionListener(this);
        connectionsList.setCellRenderer(new ConnectionConfigListCellRenderer());
        connectionsList.setFont(UIUtil.getLabelFont());


        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(connectionsList);
        decorator.setAddAction(addAction);
        decorator.setRemoveAction(removeAction);
        decorator.addExtraAction(duplicateAction);
        decorator.setMoveUpAction(moveUpAction);
        decorator.setMoveDownAction(moveDownAction);
        //decorator.addExtraAction(sortAction);
        decorator.addExtraAction(copyAction);
        decorator.addExtraAction(pasteAction);

        this.connectionListPanel.add(decorator.createPanel(), BorderLayout.CENTER);

        if (connectionBundle.getConnectionHandlers().size() > 0)
        {
            selectConnection(connectionBundle.getConnectionHandlers().get(0));
        }
        JPanel emptyPanel = new JPanel();
        connectionSetupPanel.setPreferredSize(new Dimension(500, -1));
        connectionSetupPanel.add(emptyPanel, BLANK_PANEL_ID);
        GuiUtils.replaceJSplitPaneWithIDEASplitter(mainPanel);
        GUIUtil.updateSplitterProportion(mainPanel, (float) 0.3);
    }

    public JPanel getComponent()
    {
        return mainPanel;
    }

    public void applyFormChanges() throws ConfigurationException
    {
        ConnectionBundleSettings connectionBundleSettings = getConfiguration();
        final ConnectionBundle connectionBundle = connectionBundleSettings.getConnectionBundle();

        List<ConnectionHandler> oldConnections = new ArrayList<ConnectionHandler>(connectionBundle.getConnectionHandlers().getFullList());
        List<ConnectionHandler> newConnections = new ArrayList<ConnectionHandler>();

        final AtomicBoolean listChanged = new AtomicBoolean(false);
        ConnectionListModel listModel = (ConnectionListModel) connectionsList.getModel();
        if (oldConnections.size() == listModel.getSize())
        {
            for (int i = 0; i < oldConnections.size(); i++)
            {
                ConnectionSettings oldConfig = oldConnections.get(i).getSettings();
                ConnectionSettings newConfig = ((ConnectionSettings) listModel.get(i));
                if (!oldConfig.getConnectionId().equals(newConfig.getConnectionId()) ||
                        (newConfig.getSettingsEditor() != null && newConfig.getFileSystemSettings().getSettingsEditor().isConnectionActive() != oldConfig.getFileSystemSettings().isActive()))
                {
                    listChanged.set(true);
                    break;
                }
            }
        } else
        {
            listChanged.set(true);
        }

        for (int i = 0; i < listModel.getSize(); i++)
        {
            ConnectionSettings connectionSettings = (ConnectionSettings) listModel.getElementAt(i);
            connectionSettings.apply();

            ConnectionHandler connectionHandler = connectionBundle.getConnection(connectionSettings.getConnectionId());
            if (connectionHandler == null)
            {
                connectionHandler = new ConnectionHandlerImpl(connectionBundle, connectionSettings);
                connectionSettings.setNew(false);
            } else
            {
                oldConnections.remove(connectionHandler);
                ((ConnectionHandlerImpl) connectionHandler).setConnectionConfig(connectionSettings);
            }

            newConnections.add(connectionHandler);

        }
        connectionBundle.setConnectionHandlers(newConnections);
        // dispose old list
        if (oldConnections.size() > 0)
        {
            ConnectionManager connectionManager = ConnectionManager.getInstance(connectionBundle.getProject());
            connectionManager.disposeConnections(oldConnections);
        }

        new SettingsChangeNotifier()
        {
            @Override
            public void notifyChanges()
            {
                if (listChanged.get())
                {
                    Project project = connectionBundle.getProject();
                    ConnectionBundleSettingsListener listener = EventUtil.notify(project, ConnectionBundleSettingsListener.TOPIC);
                    if (listener != null) listener.settingsChanged();
                }
            }
        };
    }

    public void resetFormChanges()
    {
        ConnectionListModel listModel = (ConnectionListModel) connectionsList.getModel();
        for (int i = 0; i < listModel.getSize(); i++)
        {
            ConnectionSettings connectionSettings = (ConnectionSettings) listModel.getElementAt(i);
            connectionSettings.reset();
        }
    }

    public void selectConnection(@Nullable ConnectionHandler connectionHandler)
    {
        if (connectionHandler != null)
        {
            connectionsList.setSelectedValue(connectionHandler.getSettings(), true);
        }
    }

    public void valueChanged(ListSelectionEvent listSelectionEvent)
    {
        try
        {
            Object selectedValue = connectionsList.getSelectedValue();
            if (selectedValue != null)
            {
                ConnectionSettings connectionSettings = (ConnectionSettings) selectedValue;
                switchSettingsPanel(connectionSettings);
            } else
            {
                switchSettingsPanel(null);
            }
        } catch (IndexOutOfBoundsException e)
        {
        }
    }

    @Override
    public void dispose()
    {
        for (ConnectionSettingsForm settingsForm : cachedForms.values())
        {
            Disposer.dispose(settingsForm);
        }
        cachedForms.clear();
        super.dispose();
    }

    private void switchSettingsPanel(ConnectionSettings connectionSettings)
    {
        CardLayout cardLayout = (CardLayout) connectionSetupPanel.getLayout();
        if (connectionSettings == null)
        {
            cardLayout.show(connectionSetupPanel, BLANK_PANEL_ID);
        } else
        {

            ConnectionSettingsForm currentForm = cachedForms.get(currentPanelId);
            String selectedTabName = currentForm == null ? null : currentForm.getSelectedTabName();

            currentPanelId = connectionSettings.getConnectionId();
            if (!cachedForms.keySet().contains(currentPanelId))
            {
                JComponent setupPanel = connectionSettings.createComponent();
                this.connectionSetupPanel.add(setupPanel, currentPanelId);
                cachedForms.put(currentPanelId, connectionSettings.getSettingsEditor());
            }

            ConnectionSettingsForm settingsEditor = connectionSettings.getSettingsEditor();
            if (settingsEditor != null)
            {
                settingsEditor.selectTab(selectedTabName);
            }

            cardLayout.show(connectionSetupPanel, currentPanelId);
        }
    }


    private AnActionButtonRunnable addAction = new AnActionButtonRunnable()
    {
        @Override
        public void run(AnActionButton anActionButton)
        {
            ConnectionBundleSettings connectionBundleSettings = getConfiguration();
            connectionBundleSettings.setModified(true);
            ConnectionSettings connectionSettings = new ConnectionSettings(connectionBundleSettings);
            connectionSettings.setNew(true);
            connectionSettings.generateNewId();

            String name = LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGNEWCONNCTIONNAME);
            ConnectionListModel model = (ConnectionListModel) connectionsList.getModel();
            while (model.getConnectionConfig(name) != null)
            {
                name = NamingUtil.getNextNumberedName(name, true);
            }
            GenericConnectionFileSystemSettings connectionConfig = (GenericConnectionFileSystemSettings) connectionSettings.getFileSystemSettings();
            connectionConfig.setName(name);
            int selectedIndex = connectionsList.getSelectedIndex() + 1;
            model.add(selectedIndex, connectionSettings);
            connectionsList.setSelectedIndex(selectedIndex);
        }
    };

    private AnActionButton duplicateAction = new AnActionButton("Duplicate fsconnection", Icons.ACTION_COPY)
    {
        @Override
        public void actionPerformed(AnActionEvent anActionEvent)
        {
            getConfiguration().setModified(true);
            ConnectionSettings connectionSettings = (ConnectionSettings) connectionsList.getSelectedValue();
            ConnectionListModel model = (ConnectionListModel) connectionsList.getModel();
            ConnectionSettings clone = connectionSettings.clone();
            clone.setNew(true);
            String name = clone.getFileSystemSettings().getName();
            while (model.getConnectionConfig(name) != null)
            {
                name = NamingUtil.getNextNumberedName(name, true);
            }
            clone.getFileSystemSettings().setName(name);
            int selectedIndex = connectionsList.getSelectedIndex() + 1;
            model.add(selectedIndex, clone);
            connectionsList.setSelectedIndex(selectedIndex);
        }
    };

    private AnActionButtonRunnable removeAction = new AnActionButtonRunnable()
    {
        @Override
        public void run(AnActionButton anActionButton)
        {
            getConfiguration().setModified(true);
            ListUtil.removeSelectedItems(connectionsList);
        }
    };

    private AnActionButtonRunnable moveUpAction = new AnActionButtonRunnable()
    {
        @Override
        public void run(AnActionButton anActionButton)
        {
            getConfiguration().setModified(true);
            ListUtil.moveSelectedItemsUp(connectionsList);
        }
    };

    private AnActionButtonRunnable moveDownAction = new AnActionButtonRunnable()
    {
        @Override
        public void run(AnActionButton anActionButton)
        {
            getConfiguration().setModified(true);
            ListUtil.moveSelectedItemsDown(connectionsList);
        }
    };

    private AnActionButton sortAction = new AnActionButton()
    {
        private SortDirection currentSortDirection = SortDirection.ASCENDING;

        @Override
        public void actionPerformed(AnActionEvent anActionEvent)
        {
            currentSortDirection = currentSortDirection == SortDirection.ASCENDING ?
                    SortDirection.DESCENDING :
                    SortDirection.ASCENDING;

            if (connectionsList.getModel().getSize() > 0)
            {
                Object selectedValue = connectionsList.getSelectedValue();
                ConnectionListModel model = (ConnectionListModel) connectionsList.getModel();
                model.sort(currentSortDirection);
                connectionsList.setSelectedValue(selectedValue, true);
                getConfiguration().setModified(true);
            }
        }

        @Override
        public void updateButton(AnActionEvent e)
        {
            Icon icon;
            String text;
            if (currentSortDirection != SortDirection.ASCENDING)
            {
                icon = Icons.ACTION_SORT_ASC;
                text = "Sort list ascending";
            } else
            {
                icon = Icons.ACTION_SORT_DESC;
                text = "Sort list descending";
            }
            Presentation presentation = e.getPresentation();
            presentation.setIcon(icon);
            presentation.setText(text);
        }
    };


    private AnActionButton copyAction = new AnActionButton(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGCOPYCILPBOARD), Icons.CONNECTION_COPY)
    {
        @Override
        public void actionPerformed(AnActionEvent anActionEvent)
        {
            Object[] configurations = connectionsList.getSelectedValues();
            try
            {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Element rootElement = new Element("fsconnection-configurations");
                for (Object o : configurations)
                {
                    ConnectionSettings configuration = (ConnectionSettings) o;
                    Element configElement = new Element("config");
                    configuration.writeConfiguration(configElement);
                    rootElement.addContent(configElement);
                }

                Document document = new Document(rootElement);
                XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
                String xmlString = outputter.outputString(document);
                clipboard.setContents(ClipboardUtil.createXmlContent(xmlString), null);
            } catch (Exception ex)
            {
                LOGGER.error("Could not copy database configuration to clipboard", ex);
            }
        }
    };

    AnActionButton pasteAction = new AnActionButton(LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.SETTINGPASTECILPBOARD), Icons.CONNECTION_PASTE)
    {
        @Override
        public void actionPerformed(AnActionEvent anActionEvent)
        {
            try
            {
                String clipboardData = ClipboardUtil.getStringContent();
                if (clipboardData != null)
                {
                    Document xmlDocument = CommonUtil.createXMLDocument(new ReaderInputStream(new StringReader(clipboardData), "UTF-8"));
                    if (xmlDocument != null)
                    {
                        Element rootElement = xmlDocument.getRootElement();
                        List<Element> configElements = rootElement.getChildren();
                        ConnectionListModel model = (ConnectionListModel) connectionsList.getModel();
                        int selectedIndex = connectionsList.getSelectedIndex();
                        List<Integer> selectedIndexes = new ArrayList<Integer>();
                        ConnectionBundleSettings configuration = getConfiguration();
                        for (Element configElement : configElements)
                        {
                            selectedIndex++;
                            ConnectionSettings clone = new ConnectionSettings(configuration);
                            clone.readConfiguration(configElement);
                            clone.setNew(true);
                            clone.generateNewId();

                            ConnectionFileSystemSettings databaseSettings = clone.getFileSystemSettings();
                            String name = databaseSettings.getName();
                            while (model.getConnectionConfig(name) != null)
                            {
                                name = NamingUtil.getNextNumberedName(name, true);
                            }
                            databaseSettings.setName(name);
                            model.add(selectedIndex, clone);
                            selectedIndexes.add(selectedIndex);
                            configuration.setModified(true);
                        }

                        connectionsList.setSelectedIndices(ArrayUtils.toPrimitive(selectedIndexes.toArray(new Integer[selectedIndexes.size()])));

                    }
                }
            } catch (Exception ex)
            {
                LOGGER.error("Could not paste database configuration from clipboard", ex);
            }
        }

        @Override
        public void updateButton(AnActionEvent e)
        {
            Presentation presentation = e.getPresentation();
            try
            {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Object clipboardData = clipboard.getData(DataFlavor.stringFlavor);
                if (clipboardData instanceof String)
                {
                    String clipboardString = (String) clipboardData;
                    presentation.setEnabled(clipboardString.contains("fsconnection-configurations"));
                } else
                {
                    presentation.setEnabled(false);
                }
            } catch (Exception ex)
            {
                presentation.setEnabled(false);
            }
        }
    };

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
        mainPanel.setLayout(new GridLayoutManager(2, 1, new Insets(8, 8, 8, 8), -1, -1));
        actionsPanel = new JPanel();
        actionsPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.add(actionsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(panel1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final JSplitPane splitPane1 = new JSplitPane();
        splitPane1.setDividerLocation(200);
        panel1.add(splitPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        connectionListPanel = new JPanel();
        connectionListPanel.setLayout(new BorderLayout(0, 0));
        connectionListPanel.setMinimumSize(new Dimension(200, 24));
        connectionListPanel.setPreferredSize(new Dimension(200, -1));
        splitPane1.setLeftComponent(connectionListPanel);
        connectionSetupPanel = new JPanel();
        connectionSetupPanel.setLayout(new CardLayout(0, 0));
        connectionSetupPanel.setPreferredSize(new Dimension(600, 0));
        splitPane1.setRightComponent(connectionSetupPanel);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$()
    {
        return mainPanel;
    }
}

