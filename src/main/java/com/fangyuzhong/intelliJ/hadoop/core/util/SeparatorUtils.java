package com.fangyuzhong.intelliJ.hadoop.core.util;

import java.util.Properties;

/**
 * Created by fangyuzhong on 17-7-28.
 */
public class SeparatorUtils
{

    static final Properties PROPERTIES = new Properties(System.getProperties());


    /**
     *
     * @return
     */
    public static String getLineSeparator()
    {
        return PROPERTIES.getProperty("line.separator");
    }


    /**
     *
     * @return
     */
    public static String getPathSeparator()
    {
        return PROPERTIES.getProperty("path.separator");
    }
}
