package com.fangyuzhong.intelliJ.hadoop.core;

import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
 public abstract class Filter<T>
{
    public static final Filter NO_FILTER = new Filter()
    {
        public boolean accepts(Object object)
        {
            return true;
        }

        public boolean acceptsAll(List objects)
        {
            return true;
        }
    };

    public abstract boolean accepts(T paramT);

    public boolean acceptsAll(List<T> objects)
    {
        for (T object : objects)
        {
            if (!accepts(object))
            {
                return false;
            }
        }
        return true;
    }
}
