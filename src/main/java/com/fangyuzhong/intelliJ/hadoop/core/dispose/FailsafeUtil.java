package com.fangyuzhong.intelliJ.hadoop.core.dispose;

import com.intellij.mock.MockProject;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.testFramework.LightVirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class FailsafeUtil
{
    private static final VirtualFile DUMMY_VIRTUAL_FILE = new LightVirtualFile();
    public static final Project DUMMY_PROJECT = new MockProject(ApplicationManager.getApplication().getPicoContainer(), ApplicationManager.getApplication());

    @NotNull
    public static <T extends Disposable> T get(@Nullable T disposable)
    {
        if ((disposable == null) || (disposable.isDisposed()))
        {
            throw AlreadyDisposedException.INSTANCE;
        }
        if (disposable == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/dispose/FailsafeUtil", "get"}));
        }
        return disposable;
    }

    @NotNull
    public static <T> T get(@Nullable T object)
    {
        if (object == null)
        {
            throw AlreadyDisposedException.INSTANCE;
        }
        if (object == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/dispose/FailsafeUtil", "get"}));
        }
        return object;
    }

    @NotNull
    public static Project get(@Nullable Project project)
    {
        if (project == null)
        {
            throw AlreadyDisposedException.INSTANCE;
        }
        if (project == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/dispose/FailsafeUtil", "get"}));
        }
        return project;
    }

    public static <T> T getComponent(@Nullable Project project, @NotNull Class<T> interfaceClass)
    {
        if (interfaceClass == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"interfaceClass", "com/dci/intellij/dbn/core/dispose/FailsafeUtil", "getComponent"}));
        }
        project = get(project);
        return (T) project.getComponent(interfaceClass);
    }

    @NotNull
    public static VirtualFile nvl(@Nullable VirtualFile virtualFile)
    {
        if ((virtualFile == null ? DUMMY_VIRTUAL_FILE : virtualFile) == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/dispose/FailsafeUtil", "nvl"}));
        }
        return virtualFile;
    }

    public static void check(Object object)
    {
        if (!softCheck(object))
        {
            throw AlreadyDisposedException.INSTANCE;
        }
    }

    public static boolean softCheck(Object object)
    {
        if (object == null)
        {
            return false;
        }
        if ((object instanceof Disposable))
        {
            Disposable disposable = (Disposable) object;
            if (disposable.isDisposed())
            {
                return false;
            }
        } else if ((object instanceof Project))
        {
            Project project = (Project) object;
            if (project.isDisposed())
            {
                return false;
            }
        }
        return true;
    }
}

