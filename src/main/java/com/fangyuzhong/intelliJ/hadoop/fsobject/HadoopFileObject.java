package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import org.apache.hadoop.fs.FileStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义HDFS文件对象
 * Created by fangyuzhong on 17-7-17.
 */
public class HadoopFileObject extends FileSystemObjectImpl
{
   private List<FileSystemBrowserTreeNode> fileSystemBrowserTreeNodes =null;
   private FileStatus fileStatus;

    /**
     * 初始化HDFS文件对象
     * @param parentObject 父对象
     * @param resultSet HDFS的FIleStatus
     * @param name 文件名称
     */
    public HadoopFileObject(FileSystemObject parentObject,
                            org.apache.hadoop.fs.FileStatus resultSet, String name)
    {
        super(parentObject, resultSet,name);
        fileSystemBrowserTreeNodes = new ArrayList();
        fileStatus = resultSet;
    }

    /**
     * 初始化HDFS的文件对象
     * @param connectionHandler 连接处理对象
     * @param resultSet HDFS的FIleStatus
     * @param name 文件名称
     */
    public HadoopFileObject(ConnectionHandler connectionHandler,
                            org.apache.hadoop.fs.FileStatus resultSet, String name)
    {
        super(connectionHandler, resultSet,name);
        fileSystemBrowserTreeNodes = new ArrayList();
        fileStatus = resultSet;
    }

    /**
     * 获取HDFS文件所在的路径
     * @return
     */
    public String getLocationString()
    {
        return fileStatus.getPath().toString();
    }

    /**
     * 获取对象类型，这里是文件
     * @return
     */
    public FileSystemObjectType getObjectType()
    {
        return FileSystemObjectType.FILE;
    }

    /**
     * 文件没有子对象，但不能给NULL
     * @return
     */
    public List<FileSystemBrowserTreeNode> buildAllPossibleTreeChildren()
    {
        return fileSystemBrowserTreeNodes;
    }
}