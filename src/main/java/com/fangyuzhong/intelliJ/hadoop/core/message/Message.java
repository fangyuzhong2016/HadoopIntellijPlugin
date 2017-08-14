package com.fangyuzhong.intelliJ.hadoop.core.message;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class Message
        extends DisposableBase
{
    protected MessageType type;
    protected String text;

    public Message(MessageType type, String text)
    {
        this.type = type;
        this.text = text;
    }

    public MessageType getType()
    {
        return this.type;
    }

    public void setType(MessageType type)
    {
        this.type = type;
    }

    public String getText()
    {
        return this.text;
    }

    public boolean isError()
    {
        return this.type == MessageType.ERROR;
    }

    public boolean isInfo()
    {
        return this.type == MessageType.INFO;
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Message))
        {
            return false;
        }
        Message message = (Message) o;
        if (!this.text.equals(message.text))
        {
            return false;
        }
        if (this.type != message.type)
        {
            return false;
        }
        return true;
    }

    public int hashCode()
    {
        int result = this.type.hashCode();
        result = 31 * result + this.text.hashCode();
        return result;
    }
}