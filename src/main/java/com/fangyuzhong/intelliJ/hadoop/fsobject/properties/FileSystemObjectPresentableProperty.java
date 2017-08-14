package com.fangyuzhong.intelliJ.hadoop.fsobject.properties;

import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObjectRef;
import com.fangyuzhong.intelliJ.hadoop.fsobject.NamingUtil;
import com.intellij.pom.Navigatable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class FileSystemObjectPresentableProperty
        extends PresentableProperty
{
    private FileSystemObjectRef objectRef;
    private boolean qualified = false;
    private String name;
    private String vlaue;

    public FileSystemObjectPresentableProperty(String name, FileSystemObject object, boolean qualified)
    {
        this.objectRef = object.getRef();
        this.qualified = qualified;
        this.name = name;
    }

    public FileSystemObjectPresentableProperty(FileSystemObject object, boolean qualified)
    {
        this.objectRef = object.getRef();
        this.qualified = qualified;
    }

    public FileSystemObjectPresentableProperty(String name, String value)
    {
        this.name= name;
        this.vlaue=value;
    }

    public FileSystemObjectPresentableProperty(FileSystemObject object)
    {
        this.objectRef = object.getRef();
    }

    public String getName()
    {
        return this.name == null ? NamingUtil.capitalize(this.objectRef.getObjectType().getName()) : this.name;
    }

    public String getValue()
    {
        return vlaue;
    }

    public Icon getIcon()
    {
        FileSystemObject object = this.objectRef.get();
        return object == null ? null : object.getIcon();
    }

    public Navigatable getNavigatable()
    {
        return this.objectRef.get();
    }
}
