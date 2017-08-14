package com.fangyuzhong.intelliJ.hadoop.fsobject;

import java.util.EnumSet;
import java.util.Set;

/**
 * 定义文件系统对象的相关属性
 * Created by fangyuzhong on 17-7-15.
 */
public class FileSystemObjectProperties
{
    private Set<FileSystemObjectProperty> properties;

    /**
     * 判断是否包含给定的属性
     * @param property
     * @return
     */
    public boolean is(FileSystemObjectProperty property)
    {
        return (this.properties != null) && (this.properties.contains(property));
    }

    /**
     * 设置指定的属性
     * @param property
     */
    public void set(FileSystemObjectProperty property)
    {
        if (this.properties == null)
        {
            this.properties = EnumSet.noneOf(FileSystemObjectProperty.class);
        }
        this.properties.add(property);
    }

    /**
     * 重置属性
     * @param property
     */
    public void unset(FileSystemObjectProperty property)
    {
        if (this.properties != null)
        {
            this.properties.remove(property);
            if (this.properties.isEmpty())
            {
                this.properties = null;
            }
        }
    }
}
