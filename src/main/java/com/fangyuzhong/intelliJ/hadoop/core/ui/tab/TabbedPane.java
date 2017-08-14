package com.fangyuzhong.intelliJ.hadoop.core.ui.tab;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposerUtil;
import com.intellij.openapi.util.ActionCallback;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class TabbedPane
        extends JBTabsImpl
        implements Disposable
{
    public TabbedPane(@NotNull com.intellij.openapi.Disposable disposable)
    {
        super(null, null, disposable);
    }

    public void select(JComponent component, boolean requestFocus)
    {
        TabInfo tabInfo = findInfo(component);
        if (tabInfo != null)
        {
            select(tabInfo, requestFocus);
        }
    }

    @NotNull
    public TabInfo addTab(TabInfo info, int index)
    {
        if (info.getComponent() != null)
        {
            registerDisposable(info);
            return super.addTab(info, index);
        }
        return info;

    }

    @NotNull
    public TabInfo addTab(TabInfo info)
    {
        registerDisposable(info);
        return super.addTab(info);
    }

    public TabInfo addTabSilently(TabInfo info, int index)
    {
        registerDisposable(info);
        return super.addTabSilently(info, index);
    }

    private void registerDisposable(TabInfo info)
    {
        Object object = info.getObject();
        if ((object instanceof com.intellij.openapi.Disposable))
        {
            com.intellij.openapi.Disposable disposable = (com.intellij.openapi.Disposable) object;
            Disposer.register(this, disposable);
        }
    }

    @NotNull
    public ActionCallback removeTab(TabInfo tabInfo)
    {
        Object object = tabInfo.getObject();
        ActionCallback actionCallback = super.removeTab(tabInfo);
        if ((object instanceof com.intellij.openapi.Disposable))
        {
            com.intellij.openapi.Disposable disposable = (com.intellij.openapi.Disposable) object;
            DisposerUtil.dispose(disposable);
            tabInfo.setObject(null);
        }
        return actionCallback;
    }

    public void dispose()
    {
        super.dispose();
    }
}
