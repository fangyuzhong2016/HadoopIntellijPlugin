package com.fangyuzhong.intelliJ.hadoop.core.util;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class ChangeTimestamp
{
    private Timestamp value;
    private long captureTime;

    public ChangeTimestamp()
    {
        this.captureTime = System.currentTimeMillis();
        this.value = new Timestamp(this.captureTime);
    }

    public ChangeTimestamp(@NotNull Timestamp value)
    {
        this.value = value;
        this.captureTime = System.currentTimeMillis();
    }

    @NotNull
    public Timestamp value()
    {
        Timestamp tmp4_1 = this.value;
        if (tmp4_1 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/util/ChangeTimestamp", "value"}));
        }
        return tmp4_1;
    }

    public boolean isDirty()
    {
        return TimeUtil.isOlderThan(this.captureTime, 30 * TimeUtil.ONE_SECOND);
    }

    public boolean isOlderThan(ChangeTimestamp changeTimestampCheck)
    {
        return this.value.before(changeTimestampCheck.value);
    }

    public String toString()
    {
        return this.value.toString();
    }
}

