package com.fangyuzhong.intelliJ.hadoop.core.dispose;

import com.intellij.openapi.progress.ProcessCanceledException;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class AlreadyDisposedException
        extends ProcessCanceledException
{
    public static final AlreadyDisposedException INSTANCE = new AlreadyDisposedException();
}