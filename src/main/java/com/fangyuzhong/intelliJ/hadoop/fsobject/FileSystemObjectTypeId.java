package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.core.util.EnumerationUtil;

/**
 * 定义对象类ID枚举
 * Created by fangyuzhong on 17-7-15.
 */
public enum FileSystemObjectTypeId
{
    /**
     * 文件系统
     */
    FILESYSTEM,
    /**
     * 目录
     */
    DIRECTORY,
    /**
     * 文件
     */
    FILE,
    /**
     * 未知
     */
    UNKNOWN;

    /**
     * 判断是否一致
     * @param objectTypeIds
     * @return
     */
    public boolean isOneOf(FileSystemObjectTypeId... objectTypeIds)
    {
        return EnumerationUtil.isOneOf(this, objectTypeIds);
    }
}
