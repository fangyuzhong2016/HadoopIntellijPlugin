package com.fangyuzhong.intelliJ.hadoop.core.util;

import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class ClipboardUtil
{
    public static XmlContent createXmlContent(String text)
    {
        return new XmlContent(text);
    }

    public static class XmlContent
            implements Transferable
    {
        private DataFlavor[] dataFlavors;
        private String content;

        public XmlContent(String text)
        {
            this.content = text;
            try
            {
                this.dataFlavors = new DataFlavor[3];
                this.dataFlavors[0] = new DataFlavor("text/xml;class=java.lang.String");
                this.dataFlavors[1] = new DataFlavor("text/rtf;class=java.lang.String");
                this.dataFlavors[2] = new DataFlavor("text/plain;class=java.lang.String");
            } catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        public DataFlavor[] getTransferDataFlavors()
        {
            return this.dataFlavors;
        }

        public boolean isDataFlavorSupported(DataFlavor flavor)
        {
            return ("text/xml".equals(flavor.getMimeType())) || ("text/rtf".equals(flavor.getMimeType())) || ("text/plain".equals(flavor.getMimeType()));
        }

        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException, IOException
        {
            return this.content;
        }
    }

    @Nullable
    public static String getStringContent()
    {
        try
        {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Object data = clipboard.getData(DataFlavor.stringFlavor);
            if ((data instanceof String))
            {
                return (String) data;
            }
            return null;
        } catch (Exception e)
        {
        }
        return null;
    }
}

