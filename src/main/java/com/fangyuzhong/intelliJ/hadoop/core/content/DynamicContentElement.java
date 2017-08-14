package com.fangyuzhong.intelliJ.hadoop.core.content;

import com.intellij.openapi.Disposable;

/**
 * 定义动态内容要素接口
 * Created by fangyuzhong on 17-7-15.
 */

public interface DynamicContentElement extends Disposable, Comparable
{
     /**
      * 是否释放
      * @return
      */
     boolean isDisposed();

     /**
      * 获取名称
      * @return
      */
     String getName();

     /**
      * 获取加载
      * @return
      */
     int getOverload();

     /**
      * 获取描述
      * @return
      */
     String getDescription();

}
