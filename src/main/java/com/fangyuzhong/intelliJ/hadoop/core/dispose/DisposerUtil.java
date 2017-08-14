package com.fangyuzhong.intelliJ.hadoop.core.dispose;

import com.fangyuzhong.intelliJ.hadoop.core.list.FiltrableList;
import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.intellij.openapi.util.Disposer;

import java.util.Collection;
import java.util.Map;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class DisposerUtil
{
    public static void dispose(com.intellij.openapi.Disposable disposable)
    {
        if (disposable != null)
        {
            Disposer.dispose(disposable);
        }
    }

    public static void dispose(com.intellij.openapi.Disposable[] array)
    {
        if ((array != null) && (array.length > 0))
        {
            for (com.intellij.openapi.Disposable disposable : array)
            {
                dispose(disposable);
            }
        }
    }

    public static void dispose(Collection<? extends com.intellij.openapi.Disposable> collection)
    {
        if ((collection instanceof FiltrableList))
        {
            FiltrableList<? extends com.intellij.openapi.Disposable> filtrableList = (FiltrableList) collection;
            collection = filtrableList.getFullList();
        }
        if ((collection != null) && (collection.size() > 0))
        {
            for (com.intellij.openapi.Disposable disposable : collection)
            {
                dispose(disposable);
            }
            collection.clear();
        }
    }

    public static void dispose(Map<?, ? extends com.intellij.openapi.Disposable> map)
    {
        if (map != null)
        {
            for (com.intellij.openapi.Disposable disposable : map.values())
            {
                dispose(disposable);
            }
            map.clear();
        }
    }

    public static void register(com.intellij.openapi.Disposable parent, Collection<? extends com.intellij.openapi.Disposable> collection)
    {
        for (com.intellij.openapi.Disposable disposable : collection)
        {
            Disposer.register(parent, disposable);
        }
    }

    public static void disposeLater(com.intellij.openapi.Disposable disposable)
    {
        new SimpleLaterInvocator()
        {
            protected void execute()
            {
                Disposer.dispose(disposable);
            }
        }.start();
    }
}
