package com.fangyuzhong.intelliJ.hadoop.fsconnection.config;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public enum SortDirection
{
    INDEFINITE(0, ""),
    ASCENDING(1, "asc"),
    DESCENDING(-1, "desc");

    private int compareAdj;
    private String sqlToken;

    SortDirection(int compareAdj, String sqlToken)
    {
        this.compareAdj = compareAdj;
        this.sqlToken = sqlToken;
    }

    public int getCompareAdj()
    {
        return compareAdj;
    }

    public boolean isIndefinite()
    {
        return this == INDEFINITE;
    }

    public String getSqlToken()
    {
        return sqlToken;
    }
}
