package com.fangyuzhong.intelliJ.hadoop.core.list;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public abstract class AbstractFiltrableList<T>
        implements FiltrableList<T>
{
    private List<T> list;

    public AbstractFiltrableList()
    {
        this.list = new ArrayList();
    }

    public AbstractFiltrableList(List<T> list)
    {
        this.list = list;
    }

    public List<T> getFullList()
    {
        return this.list;
    }

    public void sort(Comparator<? super T> comparator)
    {
        Collections.sort(this.list, comparator);
    }

    public boolean add(T o)
    {
        return this.list.add(o);
    }

    public boolean addAll(@NotNull Collection<? extends T> c)
    {
        if (c == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"c", "com/dci/intellij/dbn/core/list/AbstractFiltrableList", "addAll"}));
        }
        return this.list.addAll(c);
    }

    public boolean remove(Object o)
    {
        return this.list.remove(o);
    }

    public boolean removeAll(@NotNull Collection c)
    {
        if (c == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"c", "com/dci/intellij/dbn/core/list/AbstractFiltrableList", "removeAll"}));
        }
        return this.list.removeAll(c);
    }

    public boolean retainAll(@NotNull Collection c)
    {
        if (c == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"c", "com/dci/intellij/dbn/core/list/AbstractFiltrableList", "retainAll"}));
        }
        return this.list.retainAll(c);
    }

    public void clear()
    {
        if (!this.list.isEmpty())
        {
            this.list.clear();
        }
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    private boolean isFiltered()
    {
        return getFilter() != null;
    }

    public int size()
    {
        Filter<T> filter = getFilter();
        if (filter != null)
        {
            int count = 0;
            for (T object : this.list)
            {
                if (filter.accepts(object))
                {
                    count++;
                }
            }
            return count;
        }
        return this.list.size();
    }

    @NotNull
    public Iterator<T> iterator()
    {
        final Filter<T> filter = getFilter();
        if (filter != null)
        {
            return new Iterator<T>()
            {
                private Iterator<T> iterator = list.iterator();
                private T next = findNext();

                private T findNext()
                {
                    while (iterator.hasNext())
                    {
                        next = iterator.next();
                        if (filter.accepts(next)) return next;
                    }
                    return null;
                }

                public boolean hasNext()
                {
                    return next != null;
                }

                public T next()
                {
                    T result = next;
                    next = findNext();
                    return result;
                }

                public void remove()
                {
                    throw new UnsupportedOperationException("Iterator remove not implemented in filtrable list");
                }
            };
        }
        else
        {
            return list.iterator();
        }
    }

    @NotNull
    public Object[] toArray()
    {
        Filter<T> filter = getFilter();
        if (filter != null)
        {
            List<T> result = new ArrayList();
            for (T object : this.list)
            {
                if (filter.accepts(object))
                {
                    result.add(object);
                }
            }
            Object[] tmp71_66 = result.toArray();
            if (tmp71_66 == null)
            {
                throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/list/AbstractFiltrableList", "toArray"}));
            }
            return tmp71_66;
        }
        Object[] tmp115_110 = this.list.toArray();
        if (tmp115_110 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/list/AbstractFiltrableList", "toArray"}));
        }
        return tmp115_110;
    }

    @NotNull
    public <E> E[] toArray(@NotNull E[] e)
    {
        if (e == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"e", "com/dci/intellij/dbn/core/list/AbstractFiltrableList", "toArray"}));
        }
        Filter<T> filter = getFilter();
        if (filter != null)
        {
            List<T> result = new ArrayList();
            for (T object : this.list)
            {
                if (filter.accepts(object))
                {
                    result.add(object);
                }
            }
            return result.toArray(e);
        }
        return this.list.toArray(e);
    }

    public boolean contains(Object o)
    {
        if (isFiltered())
        {
            return indexOf(o) > -1;
        }
        return this.list.contains(o);
    }

    public int indexOf(Object o)
    {
        Filter<T> filter = getFilter();
        if (filter != null)
        {
            if (!filter.accepts((T)o))
            {
                return -1;
            }
            int index = 0;
            for (T object : this.list)
            {
                if (object.equals(o))
                {
                    return index;
                }
                if (filter.accepts(object))
                {
                    index++;
                }
            }
            return -1;
        }
        return this.list.indexOf(o);
    }

    public int lastIndexOf(Object o)
    {
        Filter<T> filter = getFilter();
        if (filter != null)
        {
            if (!filter.accepts((T)o))
            {
                return -1;
            }
            int index = size() - 1;
            for (int i = this.list.size() - 1; i > -1; i--)
            {
                T object = this.list.get(i);
                if (object.equals(o))
                {
                    return index;
                }
                if (filter.accepts(object))
                {
                    index--;
                }
            }
            return -1;
        }
        return this.list.lastIndexOf(o);
    }

    public boolean containsAll(@NotNull Collection c)
    {
        if (c == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"c", "com/dci/intellij/dbn/core/list/AbstractFiltrableList", "containsAll"}));
        }
        if (isFiltered())
        {
            List list = Arrays.asList(toArray());
            return list.containsAll(c);
        }
        return this.list.containsAll(c);
    }

    public boolean equals(Object o)
    {
        if (isFiltered())
        {
            List list = Arrays.asList(toArray());
            return list.equals(o);
        }
        return this.list.equals(o);
    }

    private int findIndex(int index)
    {
        int count = -1;
        Filter<T> filter = getFilter();
        for (int i = 0; i < this.list.size(); i++)
        {
            T object = this.list.get(i);
            if ((filter == null) || (filter.accepts(object)))
            {
                count++;
            }
            if (count == index)
            {
                return i;
            }
        }
        return -1;
    }

    public void add(int index, T element)
    {
        if (isFiltered())
        {
            int idx = findIndex(index);
            this.list.add(idx, element);
        } else
        {
            this.list.add(index, element);
        }
    }

    public boolean addAll(int index, @NotNull Collection<? extends T> c)
    {
        if (c == null)
        {
            throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"c", "com/dci/intellij/dbn/core/list/AbstractFiltrableList", "addAll"}));
        }
        if (isFiltered())
        {
            int idx = findIndex(index);
            return this.list.addAll(idx, c);
        }
        return this.list.addAll(index, c);
    }

    public T get(int index)
    {
        if (isFiltered())
        {
            int idx = findIndex(index);
            return idx == -1 ? null : this.list.get(idx);
        }
        return (T) this.list.get(index);
    }

    public T set(int index, T element)
    {
        if (isFiltered())
        {
            int idx = findIndex(index);
            this.list.set(idx, element);
            return (T) this.list.get(idx);
        }
        return (T) this.list.set(index, element);
    }

    public T remove(int index)
    {
        if (isFiltered())
        {
            int idx = findIndex(index);
            return (T) this.list.remove(idx);
        }
        return (T) this.list.remove(index);
    }

    @NotNull
    public ListIterator<T> listIterator()
    {
        throw new UnsupportedOperationException("List iterator not implemented in filtrable list");
    }

    @NotNull
    public ListIterator<T> listIterator(int index)
    {
        throw new UnsupportedOperationException("List iterator not implemented in filtrable list");
    }

    @NotNull
    public List<T> subList(int fromIndex, int toIndex)
    {
        throw new UnsupportedOperationException("Sublist not implemented in filtrable list");
    }
}
