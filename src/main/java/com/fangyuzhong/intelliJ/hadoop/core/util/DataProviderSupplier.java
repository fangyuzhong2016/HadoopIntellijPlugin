package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.intellij.openapi.actionSystem.DataProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public interface DataProviderSupplier
{
    @Nullable
     DataProvider getDataProvider();
}

