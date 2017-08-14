package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.content.VoidDynamicContent;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class LinkedContentDependency
        extends ContentDependency
{
    private GenericFileSystemElement sourceContentOwner;
    private DynamicContentType sourceContentType;

    public LinkedContentDependency(@NotNull GenericFileSystemElement sourceContentOwner, @NotNull DynamicContentType sourceContentType)
    {
        this.sourceContentOwner = sourceContentOwner;
        this.sourceContentType = sourceContentType;
        reset();
    }

    @NotNull
    public DynamicContent getSourceContent()
    {
        if (this.sourceContentOwner != null)
        {
            DynamicContent sourceContent = this.sourceContentOwner.getDynamicContent(this.sourceContentType);
            if (sourceContent != null)
            {
                DynamicContent tmp26_25 = sourceContent;
                if (tmp26_25 == null)
                {
                    throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/dependency/LinkedContentDependency", "getSourceContent"}));
                }
                return tmp26_25;
            }
        }
        VoidDynamicContent tmp64_61 = VoidDynamicContent.INSTANCE;
        if (tmp64_61 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/dependency/LinkedContentDependency", "getSourceContent"}));
        }
        return tmp64_61;
    }

    public void dispose()
    {
        this.sourceContentOwner = null;
    }
}
