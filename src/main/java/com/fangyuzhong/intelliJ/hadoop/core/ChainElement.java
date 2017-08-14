package com.fangyuzhong.intelliJ.hadoop.core;

/**
 * 定义链存储操作
 * Created by fangyuzhong on 17-7-14.
 */
public class ChainElement<T extends ChainElement<T>>
{
    private T previous;
    private T next;
    private int index = -1;

    /**
     * 初始化
     * @param previous
     */
    public ChainElement(T previous)
    {
        this.previous = previous;
        if (previous != null)
        {
            previous.setNext((T)this);
        }
    }

    /**
     *
     * @return
     */
    public int getIndex()
    {
        if (this.index == -1)
        {
            synchronized (this)
            {
                if (this.index == -1)
                {
                    T child = (T)this;
                    while (child != null)
                    {
                        this.index += 1;
                        child = child.getPrevious();
                    }
                }
            }
        }
        return this.index;
    }

    public T getPrevious()
    {
        return this.previous;
    }

    public T getNext()
    {
        return this.next;
    }

    void setNext(T next)
    {
        this.next = next;
    }

    public boolean isLast()
    {
        return this.next == null;
    }

    public boolean isFirst()
    {
        return this.previous == null;
    }
}
