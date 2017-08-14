package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.core.Icons;
import com.fangyuzhong.intelliJ.hadoop.core.content.DynamicContentType;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import gnu.trove.THashSet;

import javax.swing.*;
import java.util.*;

/**
 * 定义文件对象的类型
 * Created by fangyuzhong on 17-7-15.
 */
public enum FileSystemObjectType implements DynamicContentType
{
    /**
     * 目录
     */
    DIRECTORY(FileSystemObjectTypeId.DIRECTORY, "directory", "directories", Icons.FOLDER, Icons.FOLDER, true),
    /**
     * 文件
     */
    FILE(FileSystemObjectTypeId.FILE,"file","files",Icons.FILE,Icons.FILE,true),
    /**
     * 未知类型
     */
    UNKNOWN(FileSystemObjectTypeId.UNKNOWN, "unknown", "unknown", null, null, true);
    private FileSystemObjectTypeId typeId;
    private String name;
    private String listName;
    private String presentableListName;
    private Icon icon;
    private Icon disabledIcon;
    private Icon listIcon;
    private boolean generic;
    private FileSystemObjectType genericType;
    private Set<FileSystemObjectType> parents = new THashSet();
    private Set<FileSystemObjectType> genericParents = new THashSet();
    private Set<FileSystemObjectType> children = new THashSet();
    private Set<FileSystemObjectType> inheritingTypes = new THashSet();
    private Set<FileSystemObjectType> familyTypes;
    private Set<FileSystemObjectType> thisAsSet = new THashSet();

    private FileSystemObjectType(FileSystemObjectTypeId typeId, String name, String listName,
                                 Icon icon, Icon disabledIcon, Icon listIcon, boolean generic)
    {
        this(typeId, name, listName, icon, listIcon, generic);
        this.disabledIcon = disabledIcon;
    }

    private FileSystemObjectType(FileSystemObjectTypeId typeId, String name, String listName,
                                 Icon icon, Icon listIcon, boolean generic)
    {
        this.typeId = typeId;
        this.name = name;
        this.listName = listName;
        this.icon = icon;
        this.listIcon = listIcon;
        this.generic = generic;
        this.presentableListName = (Character.toUpperCase(listName.charAt(0)) + listName.substring(1).replace('_', ' '));

        this.thisAsSet.add(this);
    }
    public FileSystemObjectTypeId getTypeId()
    {
        return this.typeId;
    }

    public String getName()
    {
        return this.name;
    }

    public String getListName()
    {
        return this.listName;
    }

    public String getPresentableListName()
    {
        return this.presentableListName;
    }

    public Icon getIcon()
    {
        return this.icon;
    }



    public Icon getDisabledIcon()
    {
        return this.disabledIcon != null ? this.disabledIcon : this.icon;
    }

    public Icon getListIcon()
    {
        return this.listIcon;
    }

    public boolean isGeneric()
    {
        return this.generic;
    }

    public boolean isLeaf()
    {
        return this.children.isEmpty();
    }

    public Set<FileSystemObjectType> getParents()
    {
        return this.parents;
    }

    public Set<FileSystemObjectType> getGenericParents()
    {
        return this.genericParents;
    }

    public Set<FileSystemObjectType> getChildren()
    {
        return this.children;
    }

    public Set<FileSystemObjectType> getInheritingTypes()
    {
        return this.inheritingTypes;
    }

    public void addInheritingType(FileSystemObjectType objectType)
    {
        this.inheritingTypes.add(objectType);
    }

    public Set<FileSystemObjectType> getFamilyTypes()
    {
        if (this.familyTypes == null)
        {
            this.familyTypes = new HashSet();
            this.familyTypes.addAll(this.inheritingTypes);
            this.familyTypes.add(this);
        }
        return this.familyTypes;
    }

    public FileSystemObjectType getGenericType()
    {
        if (this.genericType == null)
        {
            return this;
        }
        FileSystemObjectType objectType = this.genericType;
        for (; ; )
        {
            if (objectType.genericType == null)
            {
                return objectType;
            }
            objectType = objectType.genericType;
        }
    }

    public boolean isInheriting(FileSystemObjectType objectType)
    {
        return objectType.inheritingTypes.contains(this);
    }

    public void addParent(FileSystemObjectType parent)
    {
        this.parents.add(parent);
        this.genericParents.add(parent.getGenericType());
        parent.children.add(this);
    }

    public void setGenericType(FileSystemObjectType genericType)
    {
        this.genericType = genericType;
        genericType.inheritingTypes.add(this);
    }

    public String toString()
    {
        return this.name;
    }

    public boolean isParentOf(FileSystemObjectType objectType)
    {
        return (objectType.parents.contains(this)) || (objectType.genericParents.contains(this));
    }

    public boolean isChildOf(FileSystemObjectType objectType)
    {
        return objectType.children.contains(this);
    }

    public boolean hasChild(FileSystemObjectType objectType)
    {
        for (FileSystemObjectType childObjectType : this.children)
        {
            if (childObjectType.matches(objectType))
            {
                return true;
            }
        }
        return false;
    }

    public static FileSystemObjectType getObjectType(FileSystemObjectTypeId typeId)
    {
        for (FileSystemObjectType objectType :values())
        {
            if (objectType.typeId == typeId)
            {
                return objectType;
            }
        }
        System.out.println("ERROR - [UNKNOWN] undefined fsobject type: " + typeId);
        return UNKNOWN;
    }

    public static FileSystemObjectType getObjectType(String typeName, FileSystemObjectType defaultObjectType)
    {
        FileSystemObjectType objectType = getObjectType(typeName);
        return objectType == UNKNOWN ? defaultObjectType : objectType;
    }

    public static FileSystemObjectType getObjectType(String typeName)
    {
        if (StringUtil.isEmpty(typeName))
        {
            return null;
        }
        try
        {
            return valueOf(typeName);
        } catch (IllegalArgumentException e)
        {
            typeName = typeName.replace('_', ' ');
            for (FileSystemObjectType objectType : values())
            {
                if (objectType.name.equalsIgnoreCase(typeName))
                {
                    return objectType;
                }
            }
            System.out.println("ERROR - [UNKNOWN] undefined fsobject type: " + typeName);
        }
        return UNKNOWN;
    }

    public static String toCommaSeparated(List<FileSystemObjectType> objectTypes)
    {
        StringBuilder buffer = new StringBuilder();
        for (FileSystemObjectType objectType : objectTypes)
        {
            if (buffer.length() != 0)
            {
                buffer.append(", ");
            }
            buffer.append(objectType.name);
        }
        return buffer.toString();
    }

    public static List<FileSystemObjectType> fromCommaSeparated(String objectTypes)
    {
        List<FileSystemObjectType> list = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(objectTypes, ",");
        while (tokenizer.hasMoreTokens())
        {
            String objectTypeName = tokenizer.nextToken().trim();
            list.add(getObjectType(objectTypeName));
        }
        return list;
    }

    public boolean matchesOneOf(FileSystemObjectType... objectTypes)
    {
        for (FileSystemObjectType objectType : objectTypes)
        {
            if (matches(objectType))
            {
                return true;
            }
        }
        return false;
    }

    public boolean matches(FileSystemObjectType objectType)
    {
        FileSystemObjectType thisObjectType = this;
        while (thisObjectType != null)
        {
            if (thisObjectType == objectType)
            {
                return true;
            }
            thisObjectType = thisObjectType.genericType;
        }
        FileSystemObjectType thatObjectType = objectType.genericType;
        while (thatObjectType != null)
        {
            if (thatObjectType == this)
            {
                return true;
            }
            thatObjectType = thatObjectType.genericType;
        }
        return false;
    }

    public boolean isOneOf(FileSystemObjectType... objectTypes)
    {
        for (FileSystemObjectType objectType : objectTypes)
        {
            if (objectType.matches(this))
            {
                return true;
            }
        }
        return false;
    }
}
