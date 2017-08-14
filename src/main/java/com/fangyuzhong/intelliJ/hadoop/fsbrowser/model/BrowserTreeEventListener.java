package com.fangyuzhong.intelliJ.hadoop.fsbrowser.model;

import com.fangyuzhong.intelliJ.hadoop.core.ui.tree.TreeEventType;
import com.intellij.util.messages.Topic;

import java.util.EventListener;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public  interface BrowserTreeEventListener
        extends EventListener
{
      /**
       * 通知消息类型
       */
      final Topic<BrowserTreeEventListener> TOPIC = Topic.create("Browser tree event", BrowserTreeEventListener.class);

      /**
       * 树节点Node改变事件触发的方法
       * @param paramBrowserTreeNode Node接口
       * @param paramTreeEventType 事件类型枚举
       */
      void nodeChanged(FileSystemBrowserTreeNode paramBrowserTreeNode, TreeEventType paramTreeEventType);

      /**
       * 树节点Node选中改变事件触发的方法
       */
      void selectionChanged();
}
