package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import gnu.trove.THashMap;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class DependencyRegistry
{
    private Map<String, Set<DynamicContent>> registry = new THashMap();

    public void registerDependency(String dependencyKey, DynamicContent dynamicContent)
    {
        Set<DynamicContent> dynamicContents = (Set) this.registry.get(dependencyKey);
        if (dynamicContents == null)
        {
            dynamicContents = new HashSet();
            this.registry.put(dependencyKey, dynamicContents);
        }
        dynamicContents.add(dynamicContent);
    }

    public void markContentsDirty(String dependencyKey)
    {
        Set<DynamicContent> dynamicContents = (Set) this.registry.get(dependencyKey);
        for (DynamicContent dynamicContent : dynamicContents)
        {
            dynamicContent.markDirty();
        }
    }
}