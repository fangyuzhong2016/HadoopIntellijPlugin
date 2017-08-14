package com.fangyuzhong.intelliJ.hadoop.core.ui.tab;

import com.intellij.ui.tabs.TabInfo;

import javax.swing.*;
import javax.swing.plaf.TabbedPaneUI;
import java.awt.*;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class TabbedPaneUtil
{
    public static void setSelectComponentTab(Component component)
    {
        Component parent = component.getParent();
        while (parent != null)
        {
            if ((parent instanceof JTabbedPane))
            {
                JTabbedPane tabbedPane = (JTabbedPane) parent;
                tabbedPane.setSelectedComponent(component);
                break;
            }
            component = parent;
            parent = parent.getParent();
        }
    }

    public static void setComponentTabIcon(Component component, Icon icon)
    {
        Component parent = component.getParent();
        while (parent != null)
        {
            if ((parent instanceof JTabbedPane))
            {
                JTabbedPane tabbedPane = (JTabbedPane) parent;
                int index = tabbedPane.indexOfComponent(component);
                tabbedPane.setIconAt(index, icon);
                break;
            }
            component = parent;
            parent = parent.getParent();
        }
    }

    public static int getTabIndexAt(JTabbedPane tabbedPane, int x, int y)
    {
        TabbedPaneUI tabbedPaneUI = tabbedPane.getUI();
        for (int k = 0; k < tabbedPane.getTabCount(); k++)
        {
            Rectangle rectangle = tabbedPaneUI.getTabBounds(tabbedPane, k);
            if (rectangle.contains(x, y))
            {
                return k;
            }
        }
        return -1;
    }

    public static String getSelectedTabName(TabbedPane tabbedPane)
    {
        TabInfo selectedInfo = tabbedPane.getSelectedInfo();
        return selectedInfo == null ? null : selectedInfo.getText();
    }

    public static void selectTab(TabbedPane tabbedPane, String tabName)
    {
        if (tabName != null)
        {
            for (TabInfo tabInfo : tabbedPane.getTabs())
            {
                if (tabInfo.getText().equals(tabName))
                {
                    tabbedPane.select(tabInfo, false);
                    return;
                }
            }
        }
    }
}
