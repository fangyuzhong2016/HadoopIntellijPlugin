package com.fangyuzhong.intelliJ.hadoop.core.content.loader;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class VoidDynamicContentLoader
        implements DynamicContentLoader
{
    public static final VoidDynamicContentLoader INSTANCE = new VoidDynamicContentLoader();

    public void loadContent(DynamicContent dynamicContent, boolean forceReload)
            throws DynamicContentLoadException
    {
    }

    public void reloadContent(DynamicContent dynamicContent)
            throws DynamicContentLoadException
    {
    }
}
