package com.fangyuzhong.intelliJ.hadoop.core.options;

import org.jdom.Element;

/**
 * 定义持久化配置接口
 * Created by fangyuzhong on 17-7-21.
 */
public  interface PersistentConfiguration
{
    /**
     * 读取持久化配置 config
     * @param paramElement
     */
      void readConfiguration(Element paramElement);

    /**
     * 写入持久化配置 config
     * @param paramElement
     */
      void writeConfiguration(Element paramElement);
}