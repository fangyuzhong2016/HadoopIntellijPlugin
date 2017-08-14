package com.fangyuzhong.intelliJ.hadoop.core.thread;

import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public abstract class ModalTask<T>
        extends Task.Modal
        implements RunnableTask<T>
{
    private T data;

    public ModalTask(Project project, String title, boolean canBeCancelled)
    {
        super(project, title, canBeCancelled);
    }

    public ModalTask(Project project, String title)
    {
        super(project, title, false);
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
        try
        {
            ProgressIndicator progressIndicator = ProgressManager.getInstance().getProgressIndicator();
            run(progressIndicator);
        } catch (ProcessCanceledException e)
        {
        }
    }

    public final void run(@NotNull ProgressIndicator progressIndicator)
    {
        try
        {
            progressIndicator.pushState();
            progressIndicator.setIndeterminate(true);
            execute(progressIndicator);
        }
        catch (ProcessCanceledException ignore)
        {
        }
        finally
        {
            progressIndicator.popState();
        }
    }

    protected abstract void execute(@NotNull ProgressIndicator paramProgressIndicator);

    public void start()
    {
        TaskUtil.startTask(this, getProject());
    }
}