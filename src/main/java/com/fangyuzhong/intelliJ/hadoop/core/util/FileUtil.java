package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class FileUtil
{
    public static File createFileByRelativePath(@NotNull File absoluteBase, @NotNull String relativeTail)
    {
        if (absoluteBase == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"absoluteBase", "com/dci/intellij/dbn/core/util/FileUtil", "createFileByRelativePath"}));
        }
        if (relativeTail == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"relativeTail", "com/dci/intellij/dbn/core/util/FileUtil", "createFileByRelativePath"}));
        }
        File point = absoluteBase;
        String[] parts = relativeTail.replace('\\', '/').split("/");
        for (String part : parts)
        {
            String trimmed = part.trim();
            if ((trimmed.length() != 0) &&
                    (!".".equals(trimmed)))
            {
                if ("..".equals(trimmed))
                {
                    point = point.getParentFile();
                    if (point == null)
                    {
                        return null;
                    }
                } else
                {
                    point = new File(point, trimmed);
                }
            }
        }
        return point;
    }

    public static String convertToRelativePath(Project project, String path)
    {
        if (!StringUtil.isEmptyOrSpaces(path))
        {
            VirtualFile baseDir = project.getBaseDir();
            if (baseDir != null)
            {
                File projectDir = new File(baseDir.getPath());
                String relativePath = com.intellij.openapi.util.io.FileUtil.getRelativePath(projectDir, new File(path));
                if ((relativePath != null) &&
                        (relativePath.lastIndexOf(".." + File.separatorChar) < 1))
                {
                    return relativePath;
                }
            }
        }
        return path;
    }

    public static String convertToAbsolutePath(Project project, String path)
    {
        if (!StringUtil.isEmptyOrSpaces(path))
        {
            VirtualFile baseDir = project.getBaseDir();
            if (baseDir != null)
            {
                File projectDir = new File(baseDir.getPath());
                if (new File(path).isAbsolute())
                {
                    return path;
                }
                File file = createFileByRelativePath(projectDir, path);
                return file == null ? null : file.getPath();
            }
        }
        return path;
    }
}
