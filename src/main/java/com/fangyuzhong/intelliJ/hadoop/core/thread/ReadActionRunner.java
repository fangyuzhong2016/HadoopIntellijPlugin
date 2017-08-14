package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class ReadActionRunner<T>
{
    public final T start()
    {
        Computable<T> readAction = new Computable()
        {
            public T compute()
            {
                return (T) ReadActionRunner.this.run();
            }
        };
        return (T) ApplicationManager.getApplication().runReadAction(readAction);
    }

    protected abstract T run();
}
