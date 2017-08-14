package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.VoidDynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class BasicContentDependency
        extends ContentDependency
{
    private DynamicContent sourceContent;

    public BasicContentDependency(@NotNull DynamicContent sourceContent)
    {
        this.sourceContent = sourceContent;
        reset();
    }

    @NotNull
    public DynamicContent getSourceContent()
    {
        DynamicContent tmp10_7 = ((DynamicContent) FailsafeUtil.get(this.sourceContent));
        if (tmp10_7 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/dependency/BasicContentDependency", "getSourceContent"}));
        }
        return tmp10_7;
    }

    public void dispose()
    {
        this.sourceContent = VoidDynamicContent.INSTANCE;
    }
}
