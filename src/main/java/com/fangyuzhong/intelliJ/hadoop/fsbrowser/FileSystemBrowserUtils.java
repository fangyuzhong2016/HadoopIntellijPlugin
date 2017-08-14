package com.fangyuzhong.intelliJ.hadoop.fsbrowser;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectBundle;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreePath;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class FileSystemBrowserUtils
{
    @Nullable
    public static TreePath createTreePath(FileSystemBrowserTreeNode treeNode)
    {
        boolean isTabbedMode = FileSystemBrowserManager.getInstance(treeNode.getProject()).isTabbedMode();

        int treeDepth = treeNode.getTreeDepth();
        int nodeIndex = isTabbedMode ? treeDepth - 1 : treeDepth + 1;
        if (nodeIndex < 0)
        {
            return null;
        }
        FileSystemBrowserTreeNode[] path = new FileSystemBrowserTreeNode[nodeIndex];
        while (treeNode != null)
        {
            treeDepth = treeNode.getTreeDepth();
            path[(isTabbedMode ? treeDepth - 2 : treeDepth)] = treeNode;
            if (((treeNode instanceof FileSystemBrowserManager)) || (
                    (isTabbedMode) && ((treeNode instanceof FileSystemObjectBundle))))
            {
                break;
            }
            treeNode = treeNode.getParent();
        }
        return new TreePath(path);
    }

    public static boolean treeVisibilityChanged(List<FileSystemBrowserTreeNode> possibleTreeNodes, List<FileSystemBrowserTreeNode> actualTreeNodes, Filter<FileSystemBrowserTreeNode> filter)
    {
        for (FileSystemBrowserTreeNode treeNode : possibleTreeNodes)
        {
            if (treeNode != null)
            {
                if (filter.accepts(treeNode))
                {
                    if (!actualTreeNodes.contains(treeNode))
                    {
                        return true;
                    }
                } else if (actualTreeNodes.contains(treeNode))
                {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<FileSystemBrowserTreeNode> createList(FileSystemBrowserTreeNode... treeNodes)
    {
        List<FileSystemBrowserTreeNode> treeNodeList = new ArrayList();
        for (FileSystemBrowserTreeNode treeNode : treeNodes)
        {
            if (treeNode != null)
            {
                treeNodeList.add(treeNode);
            }
        }
        return treeNodeList;
    }
}
