package com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ui;

import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionBundle;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.ConnectionSettings;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.config.SortDirection;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class ConnectionListModel extends DefaultListModel
{
    public ConnectionListModel(ConnectionBundle connectionBundle)
    {
        List<ConnectionHandler> connectionHandlers = connectionBundle.getConnectionHandlers().getFullList();
        for (ConnectionHandler connectionHandler : connectionHandlers)
        {
            addElement(connectionHandler.getSettings());
        }
    }

    public ConnectionSettings getConnectionConfig(String name)
    {
        for (int i = 0; i < getSize(); i++)
        {
            ConnectionSettings connectionSettings = (ConnectionSettings) getElementAt(i);
            if (connectionSettings.getFileSystemSettings().getName().equals(name))
            {
                return connectionSettings;
            }
        }
        return null;
    }

    public void sort(SortDirection sortDirection)
    {
        List<ConnectionSettings> list = new ArrayList<ConnectionSettings>();
        for (int i = 0; i < getSize(); i++)
        {
            ConnectionSettings connectionSettings = (ConnectionSettings) getElementAt(i);
            list.add(connectionSettings);
        }

        switch (sortDirection)
        {
            case ASCENDING:
                Collections.sort(list, ascComparator);
                break;
            case DESCENDING:
                Collections.sort(list, descComparator);
                break;
        }

        clear();
        for (ConnectionSettings connectionSettings : list)
        {
            addElement(connectionSettings);
        }
    }

    private Comparator<ConnectionSettings> ascComparator = new Comparator<ConnectionSettings>()
    {
        public int compare(ConnectionSettings connectionSettings1, ConnectionSettings connectionSettings2)
        {
            return connectionSettings1.getFileSystemSettings().getName().compareTo(connectionSettings2.getFileSystemSettings().getName());
        }
    };

    private Comparator<ConnectionSettings> descComparator = new Comparator<ConnectionSettings>()
    {
        public int compare(ConnectionSettings connectionSettings1, ConnectionSettings connectionSettings2)
        {
            return -connectionSettings1.getFileSystemSettings().getName().compareTo(connectionSettings2.getFileSystemSettings().getName());
        }
    };
}
