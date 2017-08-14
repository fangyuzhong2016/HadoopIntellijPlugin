package com.fangyuzhong.intelliJ.hadoop.core.ui.tree;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class FileSystemTreeTransferHandler
        extends TransferHandler
{
    public void exportToClipboard(JComponent comp, Clipboard clip, int action)
    {
        JTree tree = (JTree) comp;
        TreePath[] paths = tree.getSelectionPaths();
        if ((paths != null) && (paths.length > 0))
        {
            StringBuilder buffer = new StringBuilder();
            for (TreePath path : paths)
            {
                buffer.append(path.getLastPathComponent().toString());
                buffer.append("\n");
            }
            buffer.delete(buffer.length() - 1, buffer.length());

            StringSelection contents = new StringSelection(buffer.toString());
            clip.setContents(contents, null);
        }
    }
}
