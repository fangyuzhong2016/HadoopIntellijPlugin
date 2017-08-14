package com.fangyuzhong.intelliJ.hadoop.fsobject.properties;

import com.intellij.pom.Navigatable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class SimplePresentableProperty
        extends PresentableProperty
{
    private String name;
    private String value;
    private Icon icon;

    public SimplePresentableProperty(String name, String value)
    {
        this.name = name;
        this.value = value;
    }

    public SimplePresentableProperty(String name, String value, Icon icon)
    {
        this.name = name;
        this.value = value;
        this.icon = icon;
    }

    public String getName()
    {
        return this.name;
    }

    public String getValue()
    {
        return this.value;
    }

    public Icon getIcon()
    {
        return this.icon;
    }

    public Navigatable getNavigatable()
    {
        return null;
    }
}
