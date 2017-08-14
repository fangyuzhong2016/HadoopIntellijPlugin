package com.fangyuzhong.intelliJ.hadoop.core.list;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;

import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class FiltrableListImpl<T>
        extends AbstractFiltrableList<T>
{
    private Filter<T> filter;

    public FiltrableListImpl()
    {
    }

    public FiltrableListImpl(List<T> list)
    {
        super(list);
    }

    public FiltrableListImpl(List<T> list, Filter<T> filter)
    {
        super(list);
        this.filter = filter;
    }

    public FiltrableListImpl(Filter<T> filter)
    {
        this();
        this.filter = filter;
    }

    public void setFilter(Filter<T> filter)
    {
        this.filter = filter;
    }

    public Filter<T> getFilter()
    {
        return this.filter;
    }
}
