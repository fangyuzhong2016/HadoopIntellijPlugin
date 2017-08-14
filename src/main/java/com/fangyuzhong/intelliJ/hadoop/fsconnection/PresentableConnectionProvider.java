package com.fangyuzhong.intelliJ.hadoop.fsconnection;

import com.fangyuzhong.intelliJ.hadoop.core.ui.Presentable;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-15.
 */
 public interface PresentableConnectionProvider
        extends ConnectionProvider, Presentable
{
    @Nullable
     ConnectionHandler getConnectionHandler();
}
