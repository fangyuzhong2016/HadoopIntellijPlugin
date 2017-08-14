package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.ui.Presentable;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public enum FileSystemType
        implements Presentable
{
    LOCAL("LOCAL", "Local", Icons.FILE_SYSTEM_LOCAL, Icons.FILE_SYSTEM_LOCAL_LARGE, true, new FileSystemUrlPattern[]{FileSystemUrlPattern.LOCAL}),
    HDFS("HDFS", "Hdfs", Icons.FILE_SYSTEM_HDFS, Icons.FILE_SYSTEM_HDFS_LARGE, true, new FileSystemUrlPattern[]{FileSystemUrlPattern.HDFS}),
    UNKNOWN("UNKNOWN", "Unknown", null, null, true, new FileSystemUrlPattern[]{FileSystemUrlPattern.UNKNOWN});


    private String name;
    private String displayName;
    private Icon icon;
    private Icon largeIcon;
    private FileSystemUrlPattern[] urlPatterns;
    private String internalLibraryPath;
    private boolean authenticationSupported;

    private FileSystemType(String name, String displayName, Icon icon, Icon largeIcon, boolean authenticationSupported, FileSystemUrlPattern... urlPatterns)
    {
        this.name = name;
        this.displayName = displayName;
        this.icon = icon;
        this.largeIcon = largeIcon;
        this.urlPatterns = urlPatterns;
        this.authenticationSupported = authenticationSupported;
    }

    @NotNull
    public String getName()
    {
       return this.name;
    }

    @Nullable
    public String getDescription()
    {
        return null;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public Icon getIcon()
    {
        return this.icon;
    }

    public Icon getLargeIcon()
    {
        return this.largeIcon;
    }

    public boolean isAuthenticationSupported()
    {
        return this.authenticationSupported;
    }

    public FileSystemUrlPattern[] getUrlPatterns()
    {
        return this.urlPatterns;
    }

    public boolean hasUrlPattern(FileSystemUrlPattern pattern)
    {
        for (FileSystemUrlPattern urlPattern : this.urlPatterns)
        {
            if (urlPattern == pattern)
            {
                return true;
            }
        }
        return false;
    }

    public FileSystemUrlType[] getUrlTypes()
    {
        FileSystemUrlType[] urlTypes = new FileSystemUrlType[this.urlPatterns.length];
        for (int i = 0; i < this.urlPatterns.length; i++)
        {
            FileSystemUrlPattern urlPattern = this.urlPatterns[i];
            urlTypes[i] = urlPattern.getUrlType();
        }
        return urlTypes;
    }

    public FileSystemUrlPattern getDefaultUrlPattern()
    {
        return this.urlPatterns[0];
    }

    @NotNull
    public FileSystemUrlPattern resolveUrlPattern(String url)
    {
        for (FileSystemUrlPattern urlPattern : this.urlPatterns)
        {
            if (urlPattern.matches(url))
            {
                FileSystemUrlPattern tmp34_32 = urlPattern;
                if (tmp34_32 == null)
                {
                    throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/fsconnection/FileSystemType", "resolveUrlPattern"}));
                }
                return tmp34_32;
            }
        }
        FileSystemUrlPattern tmp78_75 = FileSystemUrlPattern.UNKNOWN;
        if (tmp78_75 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/fsconnection/FileSystemType", "resolveUrlPattern"}));
        }
        return tmp78_75;
    }

    @NotNull
    public static FileSystemType get(String name)
    {
        if (StringUtil.isNotEmpty(name))
        {
            for (FileSystemType fileSystemType : values())
            {
                if (name.equalsIgnoreCase(fileSystemType.name))
                {
                   return fileSystemType;
                }
            }
        }
       return UNKNOWN;
    }

    public static FileSystemType resolve(String name)
    {
        name = name == null ? "" : name.toUpperCase();
        if (name.contains("LOCAL"))
        {
            return LOCAL;
        }
        if (name.contains("HDFS"))
        {
            return HDFS;
        }
        return UNKNOWN;
    }

}
