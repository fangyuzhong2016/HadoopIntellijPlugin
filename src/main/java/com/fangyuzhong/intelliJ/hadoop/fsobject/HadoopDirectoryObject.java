package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.FileSystemBrowserUtils;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义HDFS目录对象
 * Created by fangyuzhong on 17-7-17.
 */
public class HadoopDirectoryObject extends FileSystemObjectImpl
{
   private List<FileSystemBrowserTreeNode> fileSystemBrowserTreeNodeLists =null;
   private org.apache.hadoop.fs.FileStatus fileStatus=null;

    /**
     * 初始化 HDFS目录对象
     * @param parentObject 父对象
     * @param resultSet HDFS的FileStatus
     * @param name 对象的名称
     */
    public HadoopDirectoryObject(FileSystemObject parentObject,
                                 org.apache.hadoop.fs.FileStatus resultSet, String name)
    {
        super(parentObject,resultSet,name);
        fileStatus = resultSet;
    }

    /**
     * 初始化HDFS目录对象
     * @param connectionHandler 对象的连接
     * @param resultSet HDFS的FileStatus
     * @param name 对象的名称
     */
    public HadoopDirectoryObject(ConnectionHandler connectionHandler,
                                 org.apache.hadoop.fs.FileStatus resultSet, String name)
    {
        super(connectionHandler,resultSet,name);
        fileStatus = resultSet;
        fileSystemBrowserTreeNodeLists = new ArrayList();
    }

    /**
     * 获取对象的类型 这里是目录
     * @return
     */
   public FileSystemObjectType getObjectType()
    {
        return FileSystemObjectType.DIRECTORY;
    }

    /**
     * 获取目录对象的HDFS路径
     * @return
     */
    public String getLocationString()
    {
        return fileStatus.getPath().toString();
    }

    /**
     * 重写，构建目录的下面的子对象
     * @return
     */
   public List<FileSystemBrowserTreeNode> buildAllPossibleTreeChildren()
   {
      ConnectionHandler connectionHandler = getConnectionHandler();
      FileSystem fileSystem = connectionHandler.getMainFileSystem();
       try
       {
           String path = fileStatus.getPath().toString();
           FileStatus[] fileStatuses = fileSystem.listStatus(new Path(fileStatus.getPath().toString()));
           FileSystemBrowserTreeNode[] fileSystemBrowserTreeNodes = new FileSystemBrowserTreeNode[fileStatuses.length];
           for (int i = 0; i < fileStatuses.length; i++)
           {
               FileStatus f = fileStatuses[i];
               if (f.isDirectory())
               {
                   FileSystemObject dirdbObject = new HadoopDirectoryObject(this, f,f.getPath().getName());
                   fileSystemBrowserTreeNodes[i] = dirdbObject;
               } else
               {
                   FileSystemObject filedbObject = new HadoopFileObject(this, f,f.getPath().getName());
                   fileSystemBrowserTreeNodes[i] = filedbObject;
               }
           }
           fileSystemBrowserTreeNodeLists = FileSystemBrowserUtils.createList(fileSystemBrowserTreeNodes);
       }
       catch (Exception ex)
       {

       }
       return fileSystemBrowserTreeNodeLists;
   }
}
