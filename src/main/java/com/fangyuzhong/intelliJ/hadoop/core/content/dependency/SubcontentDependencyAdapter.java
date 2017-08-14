package com.fangyuzhong.intelliJ.hadoop.core.content.dependency;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContent;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public interface SubcontentDependencyAdapter extends ContentDependencyAdapter
{
     DynamicContent getSourceContent();
}
