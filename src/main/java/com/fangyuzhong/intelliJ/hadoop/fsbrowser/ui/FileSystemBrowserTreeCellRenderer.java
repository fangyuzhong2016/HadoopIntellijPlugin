package com.fangyuzhong.intelliJ.hadoop.fsbrowser.ui;

import com.fangyuzhong.intelliJ.hadoop.core.ui.tree.TreeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.LoadInProgressTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.globalization.LanguageKeyWord;
import com.fangyuzhong.intelliJ.hadoop.globalization.LocaleLanguageManager;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ColoredTreeCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * 定义文件系统树节点的渲染器
 * Created by fangyuzhong on 17-7-16.
 */
public class FileSystemBrowserTreeCellRenderer
        implements TreeCellRenderer
{
    private DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();

    /**
     * 初始化渲染器
     * @param project
     */
    public FileSystemBrowserTreeCellRenderer(Project project)
    {

    }

    /**
     *这个方法返回一个Component控件，也就是你要设置的树中节点的显示风格，
     * 当然在实现的时候你可以继承一个Jcomponent类的子类，也可以在类中设置一个私有变量然后返回。
     * @param tree 要设置的树，对应的对象
     * @param value 节点对象，通过他你可以获得节点的数据
     * @param selected 表示如果被选中时该如何显示
     * @param expanded 表示如果出于扩展状态如何显示节点
     * @param leaf 叶子节点的显示方式
     * @param row
     * @param hasFocus 是否拥有焦点，设置拥有焦点时的显示方式
     * @return
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        return this.cellRenderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
    }

    /**
     * 内部类，实现TreeNode的渲染方式
     */
    private class DefaultTreeCellRenderer
            extends ColoredTreeCellRenderer
    {
        private DefaultTreeCellRenderer()
        {
        }

        /**
         * 获取显示的字体
         * @return
         */
        public Font getFont()
        {
            Font font = super.getFont();
            return font == null ? UIUtil.getTreeFont() : font;
        }

        /**
         * 定制渲染器
         * @param tree
         * @param value
         * @param selected
         * @param expanded
         * @param leaf
         * @param row
         * @param hasFocus
         */
        public void customizeCellRenderer(@NotNull JTree tree, Object value, boolean selected,
                                          boolean expanded, boolean leaf, int row, boolean hasFocus)
        {
            try
            {
                if ((value instanceof LoadInProgressTreeNode))
                {
                    LoadInProgressTreeNode loadInProgressTreeNode = (LoadInProgressTreeNode) value;
                    setIcon(loadInProgressTreeNode.getIcon(0));
                    append("正在加载...", SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES);
                    return;
                }
                FileSystemBrowserTreeNode treeNode = (FileSystemBrowserTreeNode) value;
                setIcon(treeNode.getIcon(0));
                boolean isDirty = false;
                String displayName;
                if ((treeNode instanceof ConnectionBundle))
                {
                    displayName = LocaleLanguageManager.getInstance().getResourceBundle().getString(LanguageKeyWord.DFSLOCATIONS);
                } else
                {
                    displayName = treeNode.getPresentableText();
                }
                boolean showBold = false;
                boolean showGrey = false;
                boolean isDisposed = false;
                if ((treeNode instanceof FileSystemObject))
                {
                    FileSystemObject object = (FileSystemObject) treeNode;

                    isDisposed = object.isDisposed();
                }

                SimpleTextAttributes textAttributes = showGrey ? SimpleTextAttributes.GRAYED_ATTRIBUTES :
                                                      showBold ? SimpleTextAttributes.REGULAR_BOLD_ATTRIBUTES :
                                                      showGrey ? SimpleTextAttributes.GRAYED_BOLD_ATTRIBUTES :
                                                      isDisposed ? SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES :
                                                              SimpleTextAttributes.REGULAR_ATTRIBUTES;
                if (displayName == null)
                {
                    displayName = "displayName null!!";
                }
                append(displayName, textAttributes);
                TreeUtil.applySpeedSearchHighlighting(tree, this, true, selected);
                String displayDetails = treeNode.getPresentableTextDetails();
                if (!StringUtil.isEmptyOrSpaces(displayDetails))
                {
                    append(" " + displayDetails, isDirty ? SimpleTextAttributes.GRAY_ITALIC_ATTRIBUTES :
                            SimpleTextAttributes.GRAY_ATTRIBUTES);
                }
                String conditionalDetails = treeNode.getPresentableTextConditionalDetails();
                if (!StringUtil.isEmptyOrSpaces(conditionalDetails))
                {
                    append(" - " + conditionalDetails, SimpleTextAttributes.GRAY_ATTRIBUTES);
                }
            } catch (ProcessCanceledException ignore)
            {
            }
        }
    }
}
