package com.fangyuzhong.intelliJ.hadoop.fsobject.action;

import com.fangyuzhong.intelliJ.hadoop.fsobject.FileSystemObject;
import com.intellij.openapi.actionSystem.DefaultActionGroup;

/**
 * Created by fangyuzhong on 17-7-23.
 */
public class ObjectActionGroup
        extends DefaultActionGroup
{
    public ObjectActionGroup(FileSystemObject object)
    {
        switch (object.getObjectType())
        {
            case FILE:
                add(new DownDirectoryOrFileAction(object.getConnectionHandler(),object));
                add(new ViewFileAction(object.getConnectionHandler(),object));
                addSeparator();
                add(new DeleteDirectoryOrFileAction(object.getConnectionHandler(),object));
                break;
            case DIRECTORY:
                add(new RefreshDirectoryAction(object));
                addSeparator();
                add(new CreateDirectoryAction(object.getConnectionHandler(),object));
                addSeparator();
                add(new DownDirectoryOrFileAction(object.getConnectionHandler(),object));
                addSeparator();
                add(new UploadDirectoryAction(object.getConnectionHandler(),object));
                add(new UploadFileAction(object.getConnectionHandler(),object));
                addSeparator();
                add(new DeleteDirectoryOrFileAction(object.getConnectionHandler(),object));
                break;
                default:
        }
    }
}
