package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.FileSystemType;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.FileSystemUrlPattern;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.FileSystemUrlType;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui.GenericFileSystemSettingsForm;
import org.jdom.Element;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class GenericConnectionFileSystemSettings extends ConnectionFileSystemSettings
{
    protected String mapReduceHost="";
    protected String mapReducePort="";
    protected String hdfsHost="";
    protected String hdfsPort="";
    public GenericConnectionFileSystemSettings(ConnectionSettings connectionSettings)
    {
        super(connectionSettings);
        //connectionSettings.
    }

    public GenericFileSystemSettingsForm createConfigurationEditor()
    {
        return new GenericFileSystemSettingsForm(this);
    }


    /**
     * 获取MR的地址
     * @return
     */
    public String getMapReduceHost()
    {
        return mapReduceHost;
    }

    /**
     * 设置MR的地址
     * @param mapReduceHost
     */
    public void setMapReduceHost(String mapReduceHost)
    {
        this.mapReduceHost=mapReduceHost;
    }

    /**
     * 获取MR的端口号
     * @return
     */
    public String getMapReducePort()
    {
        return mapReducePort;
    }

    /**
     * 设置MR的端口号
     * @param mapReducePort
     */
    public void setMapReducePort(String mapReducePort)
    {
        this.mapReducePort = mapReducePort;
    }

    /**
     * 获取HDFS的地址
     * @return
     */
    public String getHdfsHost()
    {
        return hdfsHost;
    }

    /**
     * 设置HDFS的地址
     * @param hdfsHost
     */
    public void setHdfsHost(String hdfsHost)
    {
        this.hdfsHost = hdfsHost;
    }


    /**
     * 获取HDFS的端口号
     * @return
     */
    public String getHdfsPort()
    {
        return hdfsPort;
    }

    /**
     * 设置HDFS的端口号
     * @param hdfsPort
     */
    public void setHdfsPort(String hdfsPort)
    {
        this.hdfsPort = hdfsPort;
    }

    /**
     * 获取MR的连接字符串
     * @return
     */
    public String getMapReducelUrl()
    {
        return FileSystemUrlPattern.get(FileSystemType.HDFS, FileSystemUrlType.DATABASE).getUrl(mapReduceHost,mapReducePort);
    }


    /**
     * 获取HDFS的连接字符串
     * @return
     */
    public String getHDFSUrl()
    {
         return FileSystemUrlPattern.get(FileSystemType.HDFS, FileSystemUrlType.DATABASE).getUrl(hdfsHost,hdfsPort);
    }



    public void updateHashCode()
    {
        hashCode = (name  + mapReduceHost+ mapReducePort+ hdfsHost+hdfsPort + user + password + osAuthentication).hashCode();
    }

    public GenericConnectionFileSystemSettings clone()
    {
        Element connectionElement = new Element(getConfigElementName());
        writeConfiguration(connectionElement);
        GenericConnectionFileSystemSettings clone = new GenericConnectionFileSystemSettings(getParent());
        clone.readConfiguration(connectionElement);
        clone.setConnectivityStatus(getConnectivityStatus());
        return clone;
    }

    public String getConnectionDetails()
    {
        return "连接名称:\t" + name + "\n" +
                "描述:\t" + description + "\n" +
                "HDFS地址:\t" + getHDFSUrl() + "\n" +
                "Map/Reduce（V2）地址:\t" + getMapReducelUrl() + "\n" +
                "用户:\t" + user;
    }

    /*********************************************************
     *                PersistentConfiguration                *
     *********************************************************/
    public void readConfiguration(Element element)
    {
        super.readConfiguration(element);
        if (element.getName().equals(getConfigElementName()))
        {
            mapReduceHost = getString(element, "mapreduce-host", mapReduceHost);
            mapReducePort = getString(element, "mapreduce-port", mapReducePort);
            hdfsHost = getString(element, "hdfs-host", hdfsHost);
            hdfsPort = getString(element, "hdfs-port", hdfsPort);

        } else
        {
            mapReduceHost = element.getAttributeValue("mapreduce-host");
            mapReducePort = element.getAttributeValue("mapreduce-port");
            hdfsHost = element.getAttributeValue("hdfs-host");
            hdfsPort = element.getAttributeValue("hdfs-port");
        }
    }




    public void writeConfiguration(Element element)
    {
        super.writeConfiguration(element);
        setString(element, "mapreduce-host", nvl(mapReduceHost));
        setString(element, "mapreduce-port", nvl(mapReducePort));
        setString(element, "hdfs-host", nvl(hdfsHost));
        setString(element, "hdfs-port", nvl(hdfsPort));
    }

}

