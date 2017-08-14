package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.application.ApplicationManager;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class WriteActionRunner
{
    private RunnableTask callback;

    public WriteActionRunner(RunnableTask callback)
    {
        this.callback = callback;
    }

    public WriteActionRunner()
    {
    }

    public void start()
    {
        new SimpleLaterInvocator()
        {
            protected void execute()
            {
                Runnable writeAction = new Runnable()
                {
                    public void run()
                    {
                        try
                        {
                            WriteActionRunner.this.run();
                        } finally
                        {
                            if (WriteActionRunner.this.callback != null)
                            {
                                WriteActionRunner.this.callback.start();
                            }
                        }
                    }
                };
                ApplicationManager.getApplication().runWriteAction(writeAction);
            }
        }.start();
    }

    public abstract  void run();
}
