package com.fangyuzhong.intelliJ.hadoop.fsobject;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class NamingUtil
{
    public static String getNextNumberedName(String numberedIdentifier, boolean insertWhitespace)
    {
        StringBuilder text = new StringBuilder();
        StringBuilder number = new StringBuilder();
        for (int i = numberedIdentifier.length() - 1; i >= 0; i--)
        {
            char chr = numberedIdentifier.charAt(i);
            if ('0' <= chr && chr <= '9')
            {
                number.insert(0, chr);
            } else
            {
                text.append(numberedIdentifier.substring(0, i + 1));
                break;
            }
        }
        int nr = number.length() == 0 ? 0 : Integer.parseInt(number.toString());
        nr++;
        if (insertWhitespace && nr == 1) text.append(" ");
        return text.toString() + nr;
    }

    public static String createFriendlyName(String name)
    {
        StringBuilder friendlyName = new StringBuilder(name.replace('_', ' '));
        for (int i = 0; i < friendlyName.length(); i++)
        {
            if ((i > 0) && (Character.isLetter(friendlyName.charAt(i - 1))))
            {
                char chr = friendlyName.charAt(i);
                chr = Character.toLowerCase(chr);
                friendlyName.setCharAt(i, chr);
            }
        }
        return friendlyName.toString();
    }

    public static String capitalize(String string)
    {
        string = string.toLowerCase();
        string = Character.toUpperCase(string.charAt(0)) + string.substring(1);
        return string;
    }
}
