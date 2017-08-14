package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettings;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class ConnectionInfo
{
    private FileSystemType fileSystemType;
    private String productName;
    private String productVersion;
    private String url;
    private String userName;

    public ConnectionInfo()
    {
        this.productName = "";
        this.productVersion = "";
        int index = this.productVersion.indexOf('\n');
        this.productVersion = (index > -1 ? this.productVersion.substring(0, index) : this.productVersion);
        this.url = "";
        this.userName = "";
        String prodName = this.productName.toLowerCase();
        this.fileSystemType = FileSystemType.resolve(prodName);
    }


    public ConnectionInfo(ConnectionSettings  connectionSettings)
    {
        setConnectionInfo(connectionSettings);
    }

    public void setConnectionInfo(ConnectionSettings  connectionSettings)
    {
        this.fileSystemType = connectionSettings.getFileSystemSettings().getFileSystemType();
        this.url = connectionSettings.getFileSystemSettings().getHDFSUrl();
    }

    public String toString()
    {
        return "当前文件系统："+url;
        //return "Product name:\t" + this.productName + '\n' + "Product version:\t" + this.productVersion +  '\n' + "URL:\t\t" + this.url + '\n' + "User name:\t\t" + this.userName;
    }

    public FileSystemType getDatabaseType()
    {
        return this.fileSystemType;
    }

    public String getProductName()
    {
        return this.productName;
    }

    public String getProductVersion()
    {
        return this.productVersion;
    }


    public String getUrl()
    {
        return this.url;
    }

    public String getUserName()
    {
        return this.userName;
    }


}
