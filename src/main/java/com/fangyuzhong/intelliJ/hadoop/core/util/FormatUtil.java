package com.fangyuzhong.intelliJ.hadoop.core.util;

/**
 * Created by fangyuzhong on 17-7-20.
 */
public class FormatUtil
{
    public static double pers = 1048576; //1024*1024

    /**
     * 将字节转换为M
     * @param size
     * @return
     */
    public static String sizeFormatNum2String(long size)
    {
        String s = "";
        if (size > 1024 * 1024)
            s = String.format("%.2f", (double) size / pers) + "M";
        else
            s = String.format("%.2f", (double) size / (1024)) + "KB";
        return s;
    }

    public static long sizeFormatString2Num(String str)
    {
        long size = 0;
        if (str != null)
        {
            if (str.endsWith("KB"))
                size = (long) (Double.parseDouble(str.substring(0, str.length() - 2)) * 1024);
            else if (str.endsWith("M"))
                size = (long) (Double.parseDouble(str.substring(0, str.length() - 1)) * pers);
        }
        return size;

    }
}
