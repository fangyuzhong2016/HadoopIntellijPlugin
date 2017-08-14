package com.fangyuzhong.intelliJ.hadoop.core.list;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public interface FiltrableList<T>
        extends List<T>
{
     List<T> getFullList();

    @Nullable
     Filter<T> getFilter();

     void sort(Comparator<? super T> paramComparator);
}
