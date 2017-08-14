package com.fangyuzhong.intelliJ.hadoop.core.util;

import com.fangyuzhong.intelliJ.hadoop.core.thread.ConditionalReadActionRunner;
import com.fangyuzhong.intelliJ.hadoop.core.thread.ConditionalWriteActionRunner;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by fangyuzhong on 17-7-28.
 */
public class DocumentUtil
{
    @Nullable
    public static Document getDocument(@NotNull VirtualFile virtualFile)
    {
        return new ConditionalReadActionRunner<Document>()
        {
            @Override
            protected Document run()
            {
                return FileDocumentManager.getInstance().getDocument(virtualFile);
            }
        }.start();
    }

    public static void setText(@NotNull Document document, final CharSequence text)
    {

        new ConditionalWriteActionRunner()
        {
            public void run()
            {
                FileDocumentManager manager = FileDocumentManager.getInstance();
                VirtualFile file = manager.getFile(document);
                if ((file != null) && (file.isValid()))
                {
                    boolean isReadonly = !document.isWritable();
                    try
                    {
                        document.setReadOnly(false);
                        document.setText(text);
                    } finally
                    {
                        document.setReadOnly(isReadonly);
                    }
                }
            }
        }.start();
    }
}
