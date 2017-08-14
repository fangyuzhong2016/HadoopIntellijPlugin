package com.fangyuzhong.intelliJ.hadoop.core.ui;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class OptionBundle<T extends Option>
{
    private T[] options;

    public OptionBundle(T[] options)
    {
        this.options = options;
    }

    public boolean is(T option)
    {
        if (this.options != null)
        {
            for (T opt : this.options)
            {
                if (opt == option)
                {
                    return true;
                }
            }
        }
        return false;
    }
}
