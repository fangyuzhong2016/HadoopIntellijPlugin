package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.util.Base64Converter;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class PasswordUtil
{
    public static final Logger LOGGER = LoggerFactory.createLogger();

    public static String encodePassword(String password)
    {
        try
        {
            password = StringUtil.isEmpty(password) ? "" : Base64Converter.encode(nvl(password));
        } catch (Exception e)
        {
            LOGGER.error("Error encoding password", e);
        }
        return password;
    }

    public static String decodePassword(String password)
    {
        try
        {
            password = StringUtil.isEmpty(password) ? "" : Base64Converter.decode(nvl(password));
        } catch (Exception e)
        {
        }
        return password;
    }

    private static String nvl(String value)
    {
        return value == null ? "" : value;
    }
}
