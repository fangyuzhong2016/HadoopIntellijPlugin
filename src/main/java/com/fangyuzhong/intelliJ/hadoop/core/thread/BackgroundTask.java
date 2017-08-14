package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class BackgroundTask<T>
        extends Task.Backgroundable
        implements RunnableTask<T>
{
    private static final Logger LOGGER = LoggerFactory.createLogger();
    private T data;
    private static PerformInBackgroundOption START_IN_BACKGROUND = new PerformInBackgroundOption()
    {
        public boolean shouldStartInBackground()
        {
            return true;
        }

        public void processSentToBackground()
        {
        }
    };
    private static PerformInBackgroundOption DO_NOT_START_IN_BACKGROUND = new PerformInBackgroundOption()
    {
        public boolean shouldStartInBackground()
        {
            return false;
        }

        public void processSentToBackground()
        {
        }
    };

    public BackgroundTask(@Nullable Project project, TaskInstructions instructions)
    {
        this(project, instructions.getTitle(), instructions.isStartInBackground(), instructions.isCanBeCancelled());
    }

    public BackgroundTask(@Nullable Project project, @NotNull String title, boolean startInBackground, boolean canBeCancelled)
    {
        super(project, "Hadoop文件系统 " + title, canBeCancelled, startInBackground ? START_IN_BACKGROUND : DO_NOT_START_IN_BACKGROUND);
    }

    public BackgroundTask(@Nullable Project project, @NotNull String title, boolean startInBackground)
    {
        this(project, title, startInBackground, false);
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public T getData()
    {
        return (T) this.data;
    }

    public final void run()
    {
        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
        run(progressIndicator);
    }

    public final void run(@NotNull ProgressIndicator progressIndicator)
    {
        if (progressIndicator == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"progressIndicator", "com/dci/intellij/dbn/core/thread/BackgroundTask", "run"}));
        }
        int priority = Thread.currentThread().getPriority();
        try
        {
            Thread.currentThread().setPriority(1);
            initProgressIndicator(progressIndicator, true);
            progressIndicator.pushState();

            execute(progressIndicator);
        } catch (ProcessCanceledException e)
        {
        } catch (InterruptedException e)
        {
        } catch (Exception e)
        {
            LOGGER.error("Error executing background operation.", e);
        } finally
        {
            progressIndicator.popState();
            Thread.currentThread().setPriority(priority);
        }
    }

    protected abstract void execute(@NotNull ProgressIndicator paramProgressIndicator)
            throws InterruptedException;

    public final void start()
    {
        BackgroundTask task = this;
        TaskUtil.startTask(task, getProject());
    }

    protected static void initProgressIndicator(ProgressIndicator progressIndicator, boolean indeterminate)
    {
        initProgressIndicator(progressIndicator, indeterminate, null);
    }

    protected static void initProgressIndicator(ProgressIndicator progressIndicator, final boolean indeterminate, @Nullable final String text)
    {
        new ConditionalLaterInvocator()
        {
            protected void execute()
            {
                if (progressIndicator.isRunning())
                {
                    progressIndicator.setIndeterminate(indeterminate);
                    if (text != null)
                    {
                        progressIndicator.setText(text);
                    }
                }
            }
        }.start();
    }

    public static boolean isProcessCancelled()
    {
        ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
        return (progressIndicator != null) && (progressIndicator.isCanceled());
    }
}
