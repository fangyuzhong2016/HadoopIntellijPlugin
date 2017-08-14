package com.fangyuzhong.intelliJ.hadoop.fsbrowser;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui.BrowserToolWindowForm;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentFactoryImpl;
import org.jetbrains.annotations.NotNull;

/**
 * 定义ToolWindow
 * Created by fangyuzhong on 17-7-15.
 */
public class FileSystemBrowserToolWindowFactory implements ToolWindowFactory,DumbAware
{
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow)
    {
        BrowserToolWindowForm toolWindowForm = FileSystemBrowserManager.getInstance(project).getToolWindowForm();
        ContentFactory contentFactory = new ContentFactoryImpl();
        Content content = contentFactory.createContent(toolWindowForm.getComponent(), null, false);
        toolWindow.getContentManager().addContent(content);
        toolWindow.setIcon(Icons.FILE_SYSTEM_HDFS);
    }
}
