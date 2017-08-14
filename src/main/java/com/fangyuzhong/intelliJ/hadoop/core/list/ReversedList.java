package com.fangyuzhong.intelliJ.hadoop.core.list;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class ReversedList<T>
        implements Iterable<T>
{
    private final List<T> original;

    public ReversedList(List<T> original)
    {
        this.original = original;
    }

    public Iterator<T> iterator()
    {
        final ListIterator<T> i = this.original.listIterator(this.original.size());
        return new Iterator()
        {
            public boolean hasNext()
            {
                return i.hasPrevious();
            }

            public T next()
            {
                return (T) i.previous();
            }

            public void remove()
            {
                i.remove();
            }
        };
    }

    public static <T> ReversedList<T> get(List<T> original)
    {
        return new ReversedList(original);
    }
}