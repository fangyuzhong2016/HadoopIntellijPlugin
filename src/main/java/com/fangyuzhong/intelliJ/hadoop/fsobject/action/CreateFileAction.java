package com.fangyuzhong.intelliJ.hadoop.fsobject.action;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class CreateFileAction
        extends AnAction
{

    public CreateFileAction()
    {
        super("创建文件","", Icons.ACTION_NEWFILE);
    }

    public void actionPerformed(@NotNull AnActionEvent e)
    {

    }
}