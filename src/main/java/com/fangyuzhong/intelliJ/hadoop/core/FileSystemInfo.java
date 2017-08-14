package com.fangyuzhong.intelliJ.hadoop.core;

import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.FileSystemUrlType;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class FileSystemInfo
        implements Cloneable
{
    private String host;
    private String port;
    private String fileSystem;
    private String url;

    public FileSystemInfo()
    {
    }

    public interface Default
    {
         static final FileSystemInfo HDFS = new FileSystemInfo("localhost", "8082", "hdfs", FileSystemUrlType.DATABASE);
         static final FileSystemInfo UNKNOWN = new FileSystemInfo("localhost", "1234", "fileSystem", FileSystemUrlType.DATABASE);
    }

    private FileSystemUrlType urlType = FileSystemUrlType.DATABASE;

    public FileSystemInfo(String host, String port, String fileSystem, FileSystemUrlType urlType)
    {
        this.host = host;
        this.port = port;
        this.fileSystem = fileSystem;
        this.urlType = urlType;
    }


    public boolean isEmpty()
    {
        return (StringUtil.isEmpty(this.host)) && (StringUtil.isEmpty(this.port)) &&
                (StringUtil.isEmpty(this.fileSystem));
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getUrl()
    {
        return this.url;
    }

    public String getHost()
    {
        return this.host;
    }

    public void setHost(String host)
    {
        this.host = host;
    }

    public String getPort()
    {
        return this.port;
    }

    public void setPort(String port)
    {
        this.port = port;
    }

    public String getFileSystem()
    {
        return this.fileSystem;
    }

    public void setFileSystem(String fileSystem)
    {
        this.fileSystem = fileSystem;
    }

    public FileSystemUrlType getUrlType()
    {
        return this.urlType;
    }

    public void setUrlType(FileSystemUrlType urlType)
    {
        this.urlType = urlType;
    }


    public FileSystemInfo clone()
    {
        return new FileSystemInfo(this.host, this.port, this.fileSystem, this.urlType);
    }
}
