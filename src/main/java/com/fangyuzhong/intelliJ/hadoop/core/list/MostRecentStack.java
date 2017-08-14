package com.fangyuzhong.intelliJ.hadoop.core.list;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class MostRecentStack<T>
        implements Iterable<T>
{
    private List<T> values = new ArrayList();

    public MostRecentStack()
    {
    }

    public MostRecentStack(Iterable<T> values)
    {
        for (T value : values)
        {
            this.values.add(value);
        }
    }

    public void add(T value)
    {
        this.values.add(value);
    }

    public void stack(T value)
    {
        this.values.remove(value);
        this.values.add(0, value);
    }

    @Nullable
    public T get()
    {
        return (T) (this.values.size() > 0 ? this.values.get(0) : null);
    }

    public List<T> values()
    {
        return this.values;
    }

    public void setValues(List<T> values)
    {
        this.values = values;
    }

    public Iterator<T> iterator()
    {
        return this.values.iterator();
    }
}
