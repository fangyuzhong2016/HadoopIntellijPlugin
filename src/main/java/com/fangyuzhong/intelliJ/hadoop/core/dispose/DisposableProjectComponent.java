package com.fangyuzhong.intelliJ.hadoop.core.dispose;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public interface DisposableProjectComponent
        extends Disposable
{
    @NotNull
     Project getProject();
}
