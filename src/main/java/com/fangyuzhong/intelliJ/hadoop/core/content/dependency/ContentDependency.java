package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.intellij.openapi.Disposable;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public abstract class ContentDependency
        implements Disposable
{
    private long changeTimestamp;

    @NotNull
    public abstract DynamicContent getSourceContent();

    public void reset()
    {
        this.changeTimestamp = getSourceContent().getChangeTimestamp();
    }

    public boolean isDirty()
    {
        return this.changeTimestamp != getSourceContent().getChangeTimestamp();
    }

    public void markSourcesDirty()
    {
        getSourceContent().markDirty();
    }
}

