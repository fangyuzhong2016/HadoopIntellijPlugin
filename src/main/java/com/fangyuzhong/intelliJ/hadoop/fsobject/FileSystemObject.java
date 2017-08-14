package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentElement;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.PresentableConnectionProvider;
import com.fangyuzhong.intelliJ.hadoop.fsbrowser.model.FileSystemBrowserTreeNode;
import com.fangyuzhong.intelliJ.hadoop.fsobject.properties.PresentableProperty;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * 定义文件系统对象接口
 * Created by fangyuzhong on 17-7-15.
 */
public  interface FileSystemObject extends FileSystemBrowserTreeNode,DynamicContentElement,
        Referenceable, PresentableConnectionProvider
{
    /**
     * 获取文件系统对象类型
     *
     * @return
     */
    FileSystemObjectType getObjectType();

    /**
     * 获取文件系统对象的名称
     *
     * @return
     */
    @NotNull
    String getName();

    /**
     * 文件系统对象是否加载过
     *
     * @return
     */
    int getOverload();

    /**
     * 获取文件系统对象的类型名称
     *
     * @return
     */
    String getTypeName();

    /**
     * 获取文件系统对象的图标
     *
     * @return
     */
    @Nullable
    Icon getIcon();

    /**
     * 获取文件系统对象的连接处理对象
     *
     * @return
     */
    @NotNull
    ConnectionHandler getConnectionHandler();

    /**
     * 获取文件系统对象的父对象
     *
     * @return
     */
    FileSystemObject getParentObject();

    /**
     * 获取文件系统对象的集合
     *
     * @return
     */
    @NotNull
    FileSystemObjectBundle getObjectBundle();

    /**
     * 获取导航默认对象
     *
     * @return
     */
    @Nullable
    FileSystemObject getDefaultNavigationObject();

    /**
     * 获取未能进行释放的文件系统对象
     *
     * @return
     */
    @Nullable
    FileSystemObject getUndisposedElement();

    /**
     * 获取文件系统对象的属性
     *
     * @return
     */
    FileSystemObjectProperties getProperties();

    /**
     * 获取文件系统对象可呈现的属性集合
     *
     * @return
     */
    List<PresentableProperty> getPresentableProperties();

    /**
     * 获取文件系统对象的引用
     *
     * @return
     */
    FileSystemObjectRef getRef();

    /**
     * 对象是否已验证
     *
     * @return
     */
    boolean isValid();

    /**
     * 获取文件系统对象的父对象
     *
     * @return
     */
    @NotNull
    FileSystemBrowserTreeNode getParent();
}
