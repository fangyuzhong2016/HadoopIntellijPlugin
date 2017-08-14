package com.fangyuzhong.intelliJ.hadoop.core.content.loader;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentElement;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public interface DynamicContentLoader<T extends DynamicContentElement>
{
    public static final DynamicContentLoader VOID_CONTENT_LOADER = new DynamicContentLoader()
    {
        public void loadContent(DynamicContent dynamicContent, boolean forceReload)
                throws DynamicContentLoadException
        {
        }

        public void reloadContent(DynamicContent dynamicContent)
                throws DynamicContentLoadException
        {
        }
    };

     void loadContent(DynamicContent<T> paramDynamicContent, boolean paramBoolean)
            throws DynamicContentLoadException, InterruptedException;

     void reloadContent(DynamicContent<T> paramDynamicContent)
            throws DynamicContentLoadException, InterruptedException;
}