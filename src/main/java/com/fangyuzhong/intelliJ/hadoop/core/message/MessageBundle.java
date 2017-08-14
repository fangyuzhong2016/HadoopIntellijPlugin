package com.fangyuzhong.intelliJ.hadoop.core.message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangyuzhong on 17-7-21.
 */
public class MessageBundle
{
    private List<Message> infoMessages;
    private List<Message> warningMessages;
    private List<Message> errorMessages;

    public void addMessage(Message message)
    {
        switch (message.getType())
        {
            case INFO:
                this.infoMessages = addMessage(message, this.infoMessages);
                break;
            case WARNING:
                this.warningMessages = addMessage(message, this.warningMessages);
                break;
            case ERROR:
                this.errorMessages = addMessage(message, this.errorMessages);
        }
    }

    public void addInfoMessage(String message)
    {
        addMessage(new Message(MessageType.INFO, message));
    }

    public void addWarningMessage(String message)
    {
        addMessage(new Message(MessageType.WARNING, message));
    }

    public void addErrorMessage(String message)
    {
        addMessage(new Message(MessageType.ERROR, message));
    }

    private static List<Message> addMessage(Message message, List<Message> list)
    {
        if (list == null)
        {
            list = new ArrayList();
        }
        if (!list.contains(message))
        {
            list.add(message);
        }
        return list;
    }

    public List<Message> getInfoMessages()
    {
        return this.infoMessages;
    }

    public List<Message> getWarningMessages()
    {
        return this.warningMessages;
    }

    public List<Message> getErrorMessages()
    {
        return this.errorMessages;
    }

    public boolean hasErrors()
    {
        return (this.errorMessages != null) && (this.errorMessages.size() > 0);
    }

    public boolean hasWarnings()
    {
        return (this.warningMessages != null) && (this.warningMessages.size() > 0);
    }
}
