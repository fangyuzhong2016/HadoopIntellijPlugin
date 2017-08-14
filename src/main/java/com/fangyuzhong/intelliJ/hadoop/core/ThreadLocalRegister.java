package com.fangyuzhong.intelliJ.hadoop.core;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class ThreadLocalRegister
{
    private static ThreadLocal<Set<Object>> threadLocal = new ThreadLocal();

    static
    {
        threadLocal.set(new HashSet());
    }

    private static Set<Object> getRegister()
    {
        Set<Object> register = (Set) threadLocal.get();
        if (register == null)
        {
            register = new HashSet();
            threadLocal.set(register);
        }
        return register;
    }

    public static void register(Object object)
    {
        getRegister().add(object);
    }

    public static void unregister(Object object)
    {
        getRegister().remove(object);
    }

    public static boolean isRegistered(Object object)
    {
        return getRegister().contains(object);
    }
}