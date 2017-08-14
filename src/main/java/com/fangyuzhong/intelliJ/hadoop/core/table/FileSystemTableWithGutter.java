package com.fangyuzhong.intelliJ.hadoop.core.table;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class FileSystemTableWithGutter<T extends FileSystemTableWithGutterModel>
        extends FileSystemTable<T>
{
    public FileSystemTableWithGutter(Project project, T tableModel, boolean showHeader)
    {
        super(project, tableModel, showHeader);
    }

    @NotNull
    public T getModel()
    {
        return super.getModel();
    }
}
