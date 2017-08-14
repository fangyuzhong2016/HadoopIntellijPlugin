package com.fangyuzhong.intelliJ.hadoop.core.ui;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableProjectComponent;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-14.
 */
 public interface FileSystemForm extends DisposableProjectComponent
{
     JComponent getComponent();

    @Nullable
     JComponent getPreferredFocusedComponent();
}