package com.fangyuzhong.intelliJ.hadoop.core.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class MouseUtil
{
    public static void processMouseEvent(MouseEvent e, MouseListener listener)
    {
        int id = e.getID();
        switch (id)
        {
            case 501:
                listener.mousePressed(e);
                break;
            case 502:
                listener.mouseReleased(e);
                break;
            case 500:
                listener.mouseClicked(e);
                break;
            case 505:
                listener.mouseExited(e);
                break;
            case 504:
                listener.mouseEntered(e);
        }
    }

    public static boolean isNavigationEvent(MouseEvent event)
    {
        int button = event.getButton();
        return (button == 2) || ((event.isControlDown()) && (button == 1));
    }
}