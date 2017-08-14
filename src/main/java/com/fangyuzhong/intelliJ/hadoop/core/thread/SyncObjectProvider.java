package com.fangyuzhong.intelliJ.hadoop.core.thread;

import gnu.trove.THashMap;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class SyncObjectProvider
{
    private Map<String, AtomicInteger> SYNC_OBJECTS = new THashMap();

    public synchronized Object get(String key)
    {
        if (key != null)
        {
            AtomicInteger syncObject = (AtomicInteger) this.SYNC_OBJECTS.get(key);
            if (syncObject == null)
            {
                syncObject = new AtomicInteger(1);
                this.SYNC_OBJECTS.put(key, syncObject);
            } else
            {
                syncObject.incrementAndGet();
            }
            return syncObject;
        }
        return null;
    }

    public synchronized void release(String key)
    {
        if (key != null)
        {
            AtomicInteger syncObject = (AtomicInteger) this.SYNC_OBJECTS.get(key);
            if (syncObject.decrementAndGet() == 0)
            {
                this.SYNC_OBJECTS.remove(key);
            }
        }
    }
}
