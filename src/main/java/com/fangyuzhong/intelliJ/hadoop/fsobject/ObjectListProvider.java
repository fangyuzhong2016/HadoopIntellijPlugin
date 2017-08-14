package com.fangyuzhong.intelliJ.hadoop.fsobject;

import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
 interface ObjectListProvider<T extends FileSystemObject>
{
     List<T> getObjects();
}

