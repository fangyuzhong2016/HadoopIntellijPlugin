package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public interface GenericFileSystemElement
        extends ConnectionProvider, Disposable
{
    String getName();

    @NotNull
    Project getProject();

    @Nullable
    GenericFileSystemElement getParentElement();

    GenericFileSystemElement getUndisposedElement();

    @Nullable
    DynamicContent getDynamicContent(DynamicContentType paramDynamicContentType);
}
