package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.VoidDynamicContent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class VoidContentDependency
        extends ContentDependency
{
    public static final VoidContentDependency INSTANCE = new VoidContentDependency();

    @NotNull
    public DynamicContent getSourceContent()
    {
        VoidDynamicContent tmp3_0 = VoidDynamicContent.INSTANCE;
        if (tmp3_0 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/dependency/VoidContentDependency", "getSourceContent"}));
        }
        return tmp3_0;
    }

    public void dispose()
    {
    }
}
