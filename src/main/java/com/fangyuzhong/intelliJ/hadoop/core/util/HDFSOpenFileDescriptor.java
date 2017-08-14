package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class HDFSOpenFileDescriptor extends OpenFileDescriptor
{
    private String editorProviderId;

    public HDFSOpenFileDescriptor(Project project, @NotNull VirtualFile file, int offset)
    {
        super(project, file, offset);
    }

    public HDFSOpenFileDescriptor(Project project, @NotNull VirtualFile file, int line, int col)
    {
        super(project, file, line, col);
    }

    public HDFSOpenFileDescriptor(Project project, @NotNull VirtualFile file)
    {
        super(project, file);
    }
}

