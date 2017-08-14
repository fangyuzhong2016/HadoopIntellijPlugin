package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.intellij.openapi.Disposable;

/**
 * 定义文件系统对象的集合，这里特指HDFS中的根目录对象"/"
 * Created by fangyuzhong on 17-7-15.
 */
 public interface FileSystemObjectBundle
        extends FileSystemBrowserTreeNode, Disposable
{
    /**
     * 获取文件系统连接对象
     * @return
     */
    ConnectionHandler getConnectionHandler();
}
