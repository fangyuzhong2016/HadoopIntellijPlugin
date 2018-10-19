package com.fangyuzhong.intelliJ.hadoop.core;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class Constants
{
    public static final String FS_HDFS_IMPL_KEY = "fs.hdfs.impl";
    public static final String FS_HDFS_IMPL_VALUE = "org.apache.hadoop.hdfs.DistributedFileSystem";
    public static final String FS_FILE_IMPL_KEY = "fs.file.impl";
    public static final String FS_FILE_IMPL_VALUE = "org.apache.hadoop.fs.LocalFileSystem";
    public static final String FS_DEFAULTFS_KEY = "fs.defaultFS";
    public static final String YARN_RESOURCEMANAGER_SCHEDULER_ADDRESS="yarn.resourcemanager.scheduler.address";
	/**
     *IPC 超时重试次数
     */
    public static final String IPC_CLIENT_CONNECT_MAX_RETRIES_ON_TIMEOUTS ="ipc.client.connect.max.retries.on.timeouts";
    /**
     * IPC 超时设置
     */
    public static final String IPC_CLIENT_CONNECT_TIMEOUT="ipc.client.connect.timeout";
}