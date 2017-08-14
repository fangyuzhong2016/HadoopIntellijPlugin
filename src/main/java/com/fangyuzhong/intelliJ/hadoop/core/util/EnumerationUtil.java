package com.fangyuzhong.intelliJ.hadoop.core.util;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class EnumerationUtil
{
    public static <T extends Enum<T>> boolean isOneOf(Enum<T> enumeration, Enum<T>... values)
    {
        for (Enum value : values)
        {
            if (value == enumeration)
            {
                return true;
            }
        }
        return false;
    }
}
