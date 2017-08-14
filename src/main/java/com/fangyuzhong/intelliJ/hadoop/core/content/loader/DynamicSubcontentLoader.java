package com.fangyuzhong.intelliJ.hadoop.core.content.loader;

import com.fangyuzhong.intelliJ.hadoop.core.content.DatabaseLoadMonitor;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentElement;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.SubcontentDependencyAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public abstract class DynamicSubcontentLoader<T extends DynamicContentElement>
        implements DynamicContentLoader<T>
{
    private boolean optimized;

    protected DynamicSubcontentLoader(boolean optimized)
    {
        this.optimized = optimized;
    }

    public abstract boolean match(T paramT, DynamicContent paramDynamicContent);

    public void loadContent(DynamicContent<T> dynamicContent, boolean force)
            throws DynamicContentLoadException, InterruptedException
    {
        SubcontentDependencyAdapter dependencyAdapter = (SubcontentDependencyAdapter) dynamicContent.getDependencyAdapter();

        DynamicContent sourceContent = dependencyAdapter.getSourceContent();
        boolean isBackgroundLoad = DatabaseLoadMonitor.isLoadingInBackground();
        DynamicContentLoader<T> alternativeLoader = getAlternativeLoader();
        if (((sourceContent.isLoaded()) && (!sourceContent.isLoading()) && (!sourceContent.isDirty()) && (!force)) || (isBackgroundLoad) || (alternativeLoader == null))
        {
            boolean matchedOnce = false;
            List<T> list = null;
            for (Object object : sourceContent.getAllElements())
            {
                dynamicContent.checkDisposed();

                T element = (T) object;
                if (match(element, dynamicContent))
                {
                    matchedOnce = true;
                    if (list == null)
                    {
                        list = new ArrayList();
                    }
                    list.add(element);
                } else
                {
                    if ((matchedOnce) && (this.optimized))
                    {
                        break;
                    }
                }
            }
            dynamicContent.setElements(list);
        } else
        {
            sourceContent.loadInBackground(force);
            alternativeLoader.loadContent(dynamicContent, force);
        }
    }

    public abstract  DynamicContentLoader<T> getAlternativeLoader();

    public void reloadContent(DynamicContent<T> dynamicContent)
            throws DynamicContentLoadException, InterruptedException
    {
        loadContent(dynamicContent, true);
    }
}
