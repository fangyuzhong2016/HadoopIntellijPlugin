package com.fangyuzhong.intelliJ.hadoop.core.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class ResultSetLister
{
    public static String list(String name, ResultSet resultSet)
    {
        StringBuilder buffer = new StringBuilder(name);
        try
        {
            buffer.append("\n--------------------------------\n");
            if (resultSet == null)
            {
                buffer.append("NO RESULT SET");
                return buffer.toString();
            }
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            for (int i = 0; i < columnCount; i++)
            {
                buffer.append(metaData.getColumnName(i + 1)).append('\t');
            }
            while (resultSet.next())
            {
                buffer.append("\n");
                for (int i = 0; i < columnCount; i++)
                {
                    buffer.append(resultSet.getString(i + 1)).append('\t');
                }
            }
            resultSet.close();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return buffer.toString();
    }
}
