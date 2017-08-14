package com.fangyuzhong.intelliJ.hadoop.core;

import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.RowIcon;
import gnu.trove.THashMap;

import javax.swing.*;
import java.util.Map;

/**
 * Created by fangyuzhong on 17-7-14.
 */
public class Icons
{
    private static final Map<String, Icon> REGISTERED_ICONS = new THashMap();
    public static final Icon FILE_SYSTEM_LOCAL=load("/img/LocalFileSys.png");
    public static final Icon FILE_SYSTEM_LOCAL_LARGE=load("/img/LocalFileSysLarge.png");
    public static final Icon FILE_SYSTEM_HDFS=load("/img/hadoop-logo-16x16.png");
    public static final Icon HDFS_CANNOTCONNECTION= load("/img/hadoop-logo-notconnect.png");
    public static final Icon FILE_SYSTEM_HDFS_LARGE=load("/img/hadoop-logo-24x24.png");
    public static final Icon CONNECTION_VIRTUAL = load("/img/Elephant-16x16.png");
    public static final Icon PROJECT = load("/img/connect_HDFS16.png");
    public static final Icon FOLDER = load("/img/windows_folder.png");
    public static final Icon FILE= load("/img/file.png");
    public static final Icon COMMON_ARROW_DOWN = load("/img/ComboBoxArrow.png");
    public static final Icon ACTION_COPY = load("/img/Copy.png");
    public static final Icon ACTION_SORT_ASC = load("/img/SortAscending.png");
    public static final Icon ACTION_SORT_DESC = load("/img/SortDescending.png");
    public static final Icon ACTION_ADD = load("/img/Add.png");
    public static final Icon ACTION_EDIT = load("/img/EditSource.png");
    public static final Icon ACTION_COLLAPSE_ALL = load("/img/CollapseAll.png");
    public static final Icon ACTION_EXPAND_ALL = load("/img/ExpandAll.png");
    public static final Icon ACTION_DELETE = load("/img/Delete.png");
    public static final Icon ACTION_NEWDIR=load("/img/new-folder.png");
    public static final Icon ACTION_NEWFILE=load("/img/newFile.png");
    public static final Icon ACTION_DOWN=load("/img/download.png");
    public static final Icon ACTION_REFRESH = load("/img/Synchronize.png");
    public static final Icon ACTION_UPLOAD=load("/img/upload.png");
    public static final Icon ACTION_VIEWFILE=  load("/img/viewFile.png");
    public static final Icon ACTION_CLOSE = ACTION_DELETE;
    public static final Icon BROWSER_BACK = load("/img/BrowserBack.png");
    public static final Icon BROWSER_NEXT = load("/img/BrowserForward.png");
    public static final Icon BROWSER_OBJECT_PROPERTIES = load("/img/ObjectProperties.png");
    public static final Icon CONNECTION_COPY = load("/img/CopyConnection.png");
    public static final Icon CONNECTION_PASTE = load("/img/PasteConnection.png");
    public static final Icon CONNECTION_ACTIVE = FILE_SYSTEM_HDFS;
    public static final Icon CONNECTION_INACTIVE = FILE_SYSTEM_HDFS;
    public static final Icon CONNECTION_DISABLED = HDFS_CANNOTCONNECTION;
    public static final Icon CONNECTION_NEW = HDFS_CANNOTCONNECTION;
    public static final Icon CONNECTION_INVALID = HDFS_CANNOTCONNECTION;
    public static final Icon DIALOG_INFORMATION = load("/img/Information.png");
    public static final Icon DIALOG_WARNING = load("/img/Warning.png");
    public static final Icon DIALOG_ERROR = load("/img/Error.png");
    public static final Icon DIALOG_QUESTION = load("/img/Question.png");
    public static final Icon DATABASE_NAVIGATOR = load("/img/Elephant-64x64.png");
    public static final Icon DONATE = load("/img/Donate.png");
    public static final Icon DONATE_DISABLED = load("/img/Donate.png");

    private static Icon load(String path)
    {
        return IconLoader.getIcon(path);
    }

    private static Icon load(String key, String path)
    {
        Icon icon = load(path);
        REGISTERED_ICONS.put(key, icon);
        return icon;
    }

    /**
     * 根据功能名称获取对应的图标对象
     * @param key
     * @return
     */
    public static Icon getIcon(String key)
    {
        return (Icon) REGISTERED_ICONS.get(key);
    }

    private static Icon createRowIcon(Icon left, Icon right)
    {
        RowIcon rowIcon = new RowIcon(2);
        rowIcon.setIcon(left, 0);
        rowIcon.setIcon(right, 1);
        return rowIcon;
    }
}
