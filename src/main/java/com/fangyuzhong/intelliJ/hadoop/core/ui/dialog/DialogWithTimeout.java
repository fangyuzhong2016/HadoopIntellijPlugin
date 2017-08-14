package com.fangyuzhong.intelliJ.hadoop.core.ui.dialog;

import com.fangyuzhong.intelliJ.hadoop.core.thread.SimpleLaterInvocator;
import com.fangyuzhong.intelliJ.hadoop.core.util.TimeUtil;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public abstract class DialogWithTimeout
        extends FileSystemDialog<DialogWithTimeoutForm>
{
    private Timer timeoutTimer;
    private int secondsLeft;

    protected DialogWithTimeout(Project project, String title, boolean canBeParent, int timeoutSeconds)
    {
        super(project, title, canBeParent);
        this.secondsLeft = timeoutSeconds;
        this.component = new DialogWithTimeoutForm(this.secondsLeft);
        this.timeoutTimer = new Timer("HadoopFileSystem - Timeout Dialog Task [" + getProject().getName() + "]");
        this.timeoutTimer.schedule(new TimeoutTask(), TimeUtil.ONE_SECOND, TimeUtil.ONE_SECOND);
    }

    protected void init()
    {
         this.component.setContentComponent(createContentComponent());
        super.init();
    }

    protected abstract JComponent createContentComponent();

    public abstract void doDefaultAction();

    private class TimeoutTask
            extends TimerTask
    {
        private TimeoutTask()
        {
        }

        public void run()
        {
            if (DialogWithTimeout.this.secondsLeft > 0)
            {
                DialogWithTimeout.this.secondsLeft = (DialogWithTimeout.this.secondsLeft - 1);
                DialogWithTimeout.this.component.updateTimeLeft(DialogWithTimeout.this.secondsLeft);
                if (DialogWithTimeout.this.secondsLeft == 0)
                {
                    new SimpleLaterInvocator()
                    {
                        protected void execute()
                        {
                            DialogWithTimeout.this.doDefaultAction();
                        }
                    }.start();
                }
            }
        }
    }

    public void dispose()
    {
        if (!isDisposed())
        {
            super.dispose();
            this.timeoutTimer.cancel();
            this.timeoutTimer.purge();
        }
    }
}
