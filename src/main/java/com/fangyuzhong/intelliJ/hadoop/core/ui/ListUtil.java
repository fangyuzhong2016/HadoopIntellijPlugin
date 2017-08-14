package com.fangyuzhong.intelliJ.hadoop.core.ui;

import com.fangyuzhong.intelliJ.hadoop.core.LoggerFactory;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.SelectFromListDialog;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.List;
import java.util.Set;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class ListUtil
{
    private static final Logger LOGGER = LoggerFactory.createLogger();;

    public static void notifyListDataListeners(Object source, Set<ListDataListener> listDataListeners, int fromIndex, int toIndex, int eventType)
    {
        try
        {
            ListDataEvent event = new ListDataEvent(source, eventType, fromIndex, toIndex);
            for (ListDataListener listener : listDataListeners)
            {
                switch (eventType)
                {
                    case 1:
                        listener.intervalAdded(event);
                        break;
                    case 2:
                        listener.intervalRemoved(event);
                        break;
                    case 0:
                        listener.contentsChanged(event);
                }
            }
        } catch (Exception e)
        {
            ListDataEvent event;
            LOGGER.error("Error notifying list model listeners", e);
        }
    }

    public static final SelectFromListDialog.ToStringAspect BASIC_TO_STRING_ASPECT = new SelectFromListDialog.ToStringAspect()
    {
        public String getToStirng(Object obj)
        {
            return obj.toString();
        }
    };

    public static <T> T getLast(List<T> list)
    {
        return (list == null) || (list.size() == 0) ? null : list.get(list.size() - 1);
    }

    public static <T> T getFirst(List<T> list)
    {
        return (list == null) || (list.size() == 0) ? null : list.get(0);
    }
}
