package com.fangyuzhong.intelliJ.hadoop.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class DevNullStreams
{
    public static final OutputStream OUTPUT_STREAM = new OutputStream()
    {
        public void write(int i)
                throws IOException
        {
        }
    };
    public static final InputStream INPUT_STREAM = new InputStream()
    {
        public int read()
                throws IOException
        {
            return 0;
        }
    };
}
