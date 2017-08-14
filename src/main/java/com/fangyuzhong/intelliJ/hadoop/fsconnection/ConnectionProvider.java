package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import org.jetbrains.annotations.Nullable;

/**
 *获取文件系统连接处理接口
 * Created by fangyuzhong on 17-7-15.
 */
 public interface ConnectionProvider
{
    /**
     * 获取文件系统连接处理接口
     * @return
     */
     @Nullable
     ConnectionHandler getConnectionHandler();
}
