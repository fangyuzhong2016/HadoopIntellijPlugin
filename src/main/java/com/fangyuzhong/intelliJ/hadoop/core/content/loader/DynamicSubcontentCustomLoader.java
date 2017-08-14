package com.fangyuzhong.intelliJ.hadoop.core.content.loader;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentElement;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.SubcontentDependencyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public abstract class DynamicSubcontentCustomLoader<T extends DynamicContentElement>
        implements DynamicContentLoader<T>
{
    public abstract T resolveElement(DynamicContent<T> paramDynamicContent, DynamicContentElement paramDynamicContentElement);

    public void loadContent(DynamicContent<T> dynamicContent, boolean forceReload)
            throws DynamicContentLoadException, InterruptedException
    {
        List<T> list = null;
        SubcontentDependencyAdapter dependencyAdapter = (SubcontentDependencyAdapter) dynamicContent.getDependencyAdapter();
        for (Object object : dependencyAdapter.getSourceContent().getAllElements())
        {
            dynamicContent.checkDisposed();
            DynamicContentElement sourceElement = (DynamicContentElement) object;
            T element = resolveElement(dynamicContent, sourceElement);
            if (element != null)
            {
                dynamicContent.checkDisposed();
                if (list == null)
                {
                    list = new ArrayList();
                }
                list.add(element);
            }
        }
        dynamicContent.setElements(list);
    }

    public void reloadContent(DynamicContent<T> dynamicContent)
            throws DynamicContentLoadException, InterruptedException
    {
        SubcontentDependencyAdapter dependencyAdapter = (SubcontentDependencyAdapter) dynamicContent.getDependencyAdapter();
        dependencyAdapter.getSourceContent().reload();
        loadContent(dynamicContent, true);
    }
}
