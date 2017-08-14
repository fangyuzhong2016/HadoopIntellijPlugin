package com.fangyuzhong.intelliJ.hadoop.fsobject.properties;

import com.intellij.pom.Navigatable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public abstract class PresentableProperty
{
    public abstract String getName();

    public abstract String getValue();

    public abstract Icon getIcon();

    public String toString()
    {
        return getName() + ": " + getValue();
    }

    public abstract Navigatable getNavigatable();
}

