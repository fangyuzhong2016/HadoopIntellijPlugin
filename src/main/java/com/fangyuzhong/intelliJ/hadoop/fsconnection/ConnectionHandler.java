package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.fangyuzhong.intelliJ.hadoop.core.ui.Presentable;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettings;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import com.intellij.openapi.project.Project;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;


/**
 * 定义文件系统的连接处理接口
 * Created by fangyuzhong on 17-7-15.
 */
public interface ConnectionHandler extends Disposable,ConnectionProvider, Presentable
{
    /**
     * 获取当前的intellij的工程
     *
     * @return
     */
    Project getProject();
    /**
     * 测试HDFS是否可以连接
     *@param
     * @return
     */
    boolean createTestConnection() throws IOException;

    /**
     * 获取当前HDFS的连接Configuration对象
     *
     * @return
     */
    Configuration getMainConnection();

    /**
     * 获取当前连接的HDFS文件系统对象
     *
     * @return
     */
    FileSystem getMainFileSystem();

    /**
     * 获取连接状态
     *
     * @return
     */
    ConnectionStatus getConnectionStatus();

    /**
     * 获取连接字符串
     *
     * @return
     */
    ConnectionInfo getConnectionInfo();

    /**
     * 设置连接字符串
     *
     * @param paramConnectionInfo
     */
    void setConnectionInfo(ConnectionInfo paramConnectionInfo);
    /**
     * 连接是否激活
     *
     * @return
     */
    boolean isActive();

    /**
     * 获取连接对应的文件系统
     *
     * @return
     */
    FileSystemType getFileSystemType();

    /**
     * 获取文件系统版本
     *
     * @return
     */
    double getFileSystemVersion();
    /**
     * 连接是否连接上
     *
     * @return
     */
    boolean isConnected();

    /**
     * @return
     */
    int getIdleMinutes();

    /**
     * 获取文件系统的相关信息
     *
     * @return
     */
    FileSystemInfo getFileSystemInfo();
    /**
     * 释放连接
     */
    void disconnect();

    /**
     * 获取连接的ID
     *
     * @return
     */
    String getId();

    /**
     * 获取连接的用户名称
     *
     * @return
     */
    String getUserName();

    /**
     * 获取连接显示名称
     *
     * @return
     */
    String getPresentableText();

    /**
     * 获取连接引用对象
     * @return
     */
    ConnectionHandlerRef getRef();

    /**
     * 是否是虚拟连接
     * @return
     */
    boolean isVirtual();

    /**
     * 获取连接加载监控对象
     * @return
     */
    ConnectionLoadMonitor getLoadMonitor();
    /**
     * 获取连接集合
     * @return
     */
    ConnectionBundle getConnectionBundle();
    /**
     * 是否可连接
     * @return
     */
    boolean canConnect();
    /**
     *是否是有效连接
     * @param paramBoolean
     * @return
     */
    boolean isValid(boolean paramBoolean);
    /**
     * 是否是有效连接
     * @return
     */
    boolean isValid();
    /**
     *连接关联对象的集合
     * @return
     */
     FileSystemObjectBundle getObjectBundle();
    /**
     *
     * @return
     */
     Filter<FileSystemBrowserTreeNode> getObjectTypeFilter();
    /**
     * 连接配置
     * @return
     */
    ConnectionSettings getSettings();
}
