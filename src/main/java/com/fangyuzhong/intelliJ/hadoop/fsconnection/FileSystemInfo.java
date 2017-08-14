package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class FileSystemInfo
        implements Cloneable
{
    private String host;
    private String port;
    private String database;
    private String url;

    public FileSystemInfo()
    {
    }

    public  interface Default
    {
        public  final FileSystemInfo HDFS = new FileSystemInfo("localhost", "8802", "HDFS", FileSystemUrlType.DATABASE);
        public  final FileSystemInfo LOCAL = new FileSystemInfo("localhost", "", "LOCAL", FileSystemUrlType.DATABASE);
        public  final FileSystemInfo UNKNOWN = new FileSystemInfo("localhost", "1234", "UNKNOWN", FileSystemUrlType.DATABASE);
    }

    private FileSystemUrlType urlType = FileSystemUrlType.DATABASE;

    public FileSystemInfo(String host, String port, String database, FileSystemUrlType urlType)
    {
        this.host = host;
        this.port = port;
        this.database = database;
        this.urlType = urlType;
    }

    public FileSystemInfo(String file, FileSystemUrlType urlType)
    {
        this.urlType = urlType;
    }

    public boolean isEmpty()
    {
        return (StringUtil.isEmpty(this.host)) && (StringUtil.isEmpty(this.port)) && (StringUtil.isEmpty(this.database));
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

    public String getDatabase()
    {
        return this.database;
    }

    public void setDatabase(String database)
    {
        this.database = database;
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
        return new FileSystemInfo(this.host, this.port, this.database, this.urlType);
    }
}

