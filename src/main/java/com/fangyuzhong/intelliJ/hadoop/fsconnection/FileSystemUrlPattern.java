package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.util.CommonUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public enum FileSystemUrlPattern
{
    HDFS("hdfs://<HOST>:<PORT>", "^(hdfs:\\/\\/)(?<HOST>[._\\-a-z0-9]{1,1000})(?<PORT>:[0-9]{1,100})$", FileSystemInfo.Default.HDFS, FileSystemUrlType.DATABASE),
    LOCAL("local://<HOST>:<PORT>", "^(local:\\/\\/)(?<HOST>[._\\-a-z0-9]{1,1000})(?<PORT>:[0-9]{1,100})?$", FileSystemInfo.Default.LOCAL, FileSystemUrlType.DATABASE),
    UNKNOWN("unknown://<HOST>:<PORT>", "^(unknown:\\/\\/)(?<HOST>[._\\-a-z0-9]{1,1000})(?<PORT>:[0-9]{1,100})?(?<FILESYSTEM>\\/[\\-$_a-z0-9]{0,1000})?$", FileSystemInfo.Default.UNKNOWN, FileSystemUrlType.DATABASE);

    private FileSystemUrlType urlType;
    private String urlPattern;
    private String urlRegex;
    private FileSystemInfo defaultInfo;

    public static FileSystemUrlPattern get(@NotNull FileSystemType fileSystemType, @NotNull FileSystemUrlType urlType)
    {
        for (FileSystemUrlPattern urlPattern : values())
        {
            if ((fileSystemType.hasUrlPattern(urlPattern)) && (urlPattern.getUrlType() == urlType))
            {
                return urlPattern;
            }
        }
        return fileSystemType.getDefaultUrlPattern();
    }

    public String getUrl(FileSystemInfo databaseInfo)
    {
        return getUrl(databaseInfo.getHost(), databaseInfo.getPort());
    }

    public String getUrl(String host, String port)
    {
        return this.urlPattern.replace("<HOST>", (CharSequence) CommonUtil.nvl(host, "")).replace(":<PORT>", ":" + port);
    }

    public String getDefaultUrl()
    {
        return getUrl(this.defaultInfo);
    }

    public FileSystemUrlType getUrlType()
    {
        return this.urlType;
    }

    private FileSystemUrlPattern(String urlPattern, String urlRegex, FileSystemInfo defaultInfo, FileSystemUrlType urlType)
    {
        this.urlPattern = urlPattern;
        this.urlRegex = urlRegex;
        this.defaultInfo = defaultInfo;
        this.urlType = urlType;
    }

    @NotNull
    public FileSystemInfo getDefaultInfo()
    {
       return this.defaultInfo.clone();
    }

    public String resolveHost(String url)
    {
        if ((this.urlType != FileSystemUrlType.FILE) &&
                (StringUtil.isNotEmpty(url)))
        {
            Matcher matcher = getMatcher(url);
            if (matcher.matches())
            {
                return matcher.group("HOST");
            }
        }
        return "";
    }

    public String resolvePort(String url)
    {
        if ((this.urlType != FileSystemUrlType.FILE) &&
                (StringUtil.isNotEmpty(url)))
        {
            Matcher matcher = getMatcher(url);
            if (matcher.matches())
            {
                String portGroup = matcher.group("PORT");
                if (StringUtil.isNotEmpty(portGroup))
                {
                    return portGroup.substring(1);
                }
            }
        }
        return "";
    }



    public boolean isValid(String url)
    {
        if (StringUtil.isNotEmpty(url))
        {
            Matcher matcher = getMatcher(url);
            return matcher.matches();
        }
        return false;
    }

    @NotNull
    private Matcher getMatcher(String url)
    {
        Pattern pattern = Pattern.compile(this.urlRegex, 2);
        return pattern.matcher(url);

    }

    public boolean matches(String url)
    {
        return isValid(url);
    }
}

