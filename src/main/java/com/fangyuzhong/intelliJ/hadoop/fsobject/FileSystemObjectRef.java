package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.core.PersistentStateElement;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.util.StringUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionCache;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionManager;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public class FileSystemObjectRef<T extends FileSystemObject>
        implements Comparable, Reference<T>, PersistentStateElement<Element>
{
    protected FileSystemObjectRef parent;
    protected FileSystemObjectType objectType;
    protected String objectName;
    protected String connectionId;
    protected int overload;
    private WeakReference<T> reference;
    private int hashCode = -1;

    public FileSystemObjectRef(String connectionId, String identifier)
    {
        deserialize(connectionId, identifier);
    }

    public FileSystemObjectRef(T object, FileSystemObjectType fileSystemObjectType, ConnectionHandler connectionHandler)
    {
        this.reference = new WeakReference(object);
        this.objectType = fileSystemObjectType;
        this.objectName = object.getName();
        this.overload = object.getOverload();
        FileSystemObject parentObj = object.getParentObject();
        if (parentObj != null)
        {
            this.parent = parentObj.getRef();
        } else
        {
            if (connectionHandler != null)
            {
                this.connectionId = connectionHandler.getId();
            } else
            {
                this.connectionId = UUID.randomUUID().toString();
            }
        }
    }

    public FileSystemObjectRef(FileSystemObjectRef parent, FileSystemObjectType objectType, String objectName)
    {
        this.parent = parent;
        this.objectType = objectType;
        this.objectName = objectName;
    }

    public FileSystemObjectRef(String connectionId, FileSystemObjectType objectType, String objectName)
    {
        this.connectionId = connectionId;
        this.objectType = objectType;
        this.objectName = objectName;
    }

    public FileSystemObjectRef()
    {
    }

    public FileSystemObject getParentObject(FileSystemObjectType objectType)
    {
        FileSystemObjectRef parentRef = getParentRef(objectType);
        return get(parentRef);
    }

    public FileSystemObjectRef getParentRef(FileSystemObjectType objectType)
    {
        FileSystemObjectRef parent = this;
        while (parent != null)
        {
            if (parent.objectType.matches(objectType))
            {
                return parent;
            }
            parent = parent.parent;
        }
        return null;
    }

    public static FileSystemObjectRef from(Element element)
    {
        String objectRefDefinition = element.getAttributeValue("fsobject-ref");
        if (StringUtil.isNotEmpty(objectRefDefinition))
        {
            FileSystemObjectRef objectRef = new FileSystemObjectRef();
            objectRef.readState(element);
            return objectRef;
        }
        return null;
    }

    public void readState(Element element)
    {
        if (element != null)
        {
            String connectionId = element.getAttributeValue("fsconnection-id");
            String objectIdentifier = element.getAttributeValue("fsobject-ref");
            deserialize(connectionId, objectIdentifier);
        }
    }

    public void deserialize(String connectionId, String objectIdentifier)
    {
        int typeEndIndex = objectIdentifier.indexOf("]");
        StringTokenizer objectTypes = new StringTokenizer(objectIdentifier.substring(1, typeEndIndex), "/");

        int objectStartIndex = typeEndIndex + 2;
        int objectEndIndex = objectIdentifier.lastIndexOf("]");

        StringTokenizer objectNames = new StringTokenizer(objectIdentifier.substring(objectStartIndex, objectEndIndex), "/");

        FileSystemObjectRef objectRef = null;
        while (objectTypes.hasMoreTokens())
        {
            String objectTypeName = objectTypes.nextToken();
            String objectName = objectNames.nextToken();
            FileSystemObjectType objectType = FileSystemObjectType.getObjectType(objectTypeName);
            if (objectTypes.hasMoreTokens())
            {
                objectRef = objectRef == null ? new FileSystemObjectRef(connectionId, objectType, objectName) : new FileSystemObjectRef(objectRef, objectType, objectName);
            } else
            {
                if (objectNames.hasMoreTokens())
                {
                    String overloadToken = objectNames.nextToken();
                    this.overload = Integer.parseInt(overloadToken);
                }
                this.parent = objectRef;
                this.objectType = objectType;
                this.objectName = objectName;
            }
        }
    }

    public void writeState(Element element)
    {
        String value = serialize();

        element.setAttribute("fsconnection-id", getConnectionId());
        element.setAttribute("fsobject-ref", value);
    }

    @NotNull
    public String serialize()
    {
        StringBuilder objectTypes = new StringBuilder(this.objectType.getName());
        StringBuilder objectNames = new StringBuilder(this.objectName);

        FileSystemObjectRef parent = this.parent;
        while (parent != null)
        {
            objectTypes.insert(0, "/");
            objectTypes.insert(0, parent.objectType.getName());
            objectNames.insert(0, "/");
            objectNames.insert(0, parent.objectName);
            parent = parent.parent;
        }
        if (this.overload > 0)
        {
            objectNames.append("/");
            objectNames.append(this.overload);
        }
        String tmp144_138 = ("[" + objectTypes + "]" + "[" + objectNames + "]");
        if (tmp144_138 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/fsobject/lookup/FileSystemObjectRef", "serialize"}));
        }
        return tmp144_138;
    }

    public String getPath()
    {
        FileSystemObjectRef parent = this.parent;
        if (parent == null)
        {
            return this.objectName;
        }
        StringBuilder buffer = new StringBuilder(this.objectName);
        while (parent != null)
        {
            buffer.insert(0, "/");
            buffer.insert(0, parent.objectName);
            parent = parent.parent;
        }
        return buffer.toString();
    }


    public String getQualifiedNameWithType()
    {
        return this.objectType.getName() + " \"" + getPath() + "\"";
    }

    public String getTypePath()
    {
        FileSystemObjectRef parent = this.parent;
        if (parent == null)
        {
            return this.objectType.getName();
        }
        StringBuilder buffer = new StringBuilder(this.objectType.getName());
        while (parent != null)
        {
            buffer.insert(0, '.');
            buffer.insert(0, parent.objectType.getName());
            parent = parent.parent;
        }
        return buffer.toString();
    }

    public String getConnectionId()
    {
        return this.parent == null ? this.connectionId : this.parent.getConnectionId();
    }

    public FileSystemObjectRef getParent()
    {
        return this.parent;
    }

    public boolean is(@NotNull T object)
    {
        return object.getRef().equals(this);
    }

    @Nullable
    public static <T extends FileSystemObject> FileSystemObjectRef<T> from(T object)
    {
        return object == null ? null : object.getRef();
    }

    @Nullable
    public static <T extends FileSystemObject> T get(FileSystemObjectRef<T> objectRef)
    {
        return objectRef == null ? null : objectRef.get();
    }

    @NotNull
    public static <T extends FileSystemObject> T getnn(FileSystemObjectRef<T> objectRef)
    {
        T object = get(objectRef);
        return ((T) FailsafeUtil.get(object));

    }

    public static List<FileSystemObject> get(List<FileSystemObjectRef> objectRefs)
    {
        List<FileSystemObject> objects = new ArrayList(objectRefs.size());
        for (FileSystemObjectRef objectRef : objectRefs)
        {
            objects.add(get(objectRef));
        }
        return objects;
    }

    public static List<FileSystemObject> getnn(List<FileSystemObjectRef> objectRefs)
    {
        List<FileSystemObject> objects = new ArrayList(objectRefs.size());
        for (FileSystemObjectRef objectRef : objectRefs)
        {
            objects.add(getnn(objectRef));
        }
        return objects;
    }

    public static List<FileSystemObjectRef> from(List<FileSystemObject> objects)
    {
        List<FileSystemObjectRef> objectRefs = new ArrayList(objects.size());
        for (FileSystemObject object : objects)
        {
            objectRefs.add(from(object));
        }
        return objectRefs;
    }

    @Nullable
    public T get()
    {
        return load(null);
    }

    @Nullable
    public T get(Project project)
    {
        return load(project);
    }

    protected final T load(Project project)
    {
        T object = getObject();
        if (object == null)
        {
            synchronized (this)
            {
                object = getObject();
                if (object == null)
                {
                    clearReference();
                    ConnectionHandler connectionHandler = (project == null) || (project.isDisposed()) ? ConnectionCache.findConnectionHandler(getConnectionId()) : ConnectionManager.getInstance(project).getConnectionHandler(getConnectionId());
                    if ((connectionHandler != null) && (!connectionHandler.isDisposed()) && (connectionHandler.isActive()))
                    {
                        object = null;
                        if (object != null)
                        {
                            this.reference = new WeakReference(object);
                        }
                    }
                }
            }
        }
        return object;
    }

    private T getObject()
    {
        try
        {
            if (this.reference == null)
            {
                return null;
            }
            T object = (T) this.reference.get();
            if ((object == null) || (object.isDisposed()))
            {
                return null;
            }
            return object;
        } catch (Exception e)
        {
        }
        return null;
    }

    private void clearReference()
    {
        try
        {
            if (this.reference != null)
            {
                this.reference.clear();
                this.reference = null;
            }
        } catch (Exception ignore)
        {
        }
    }

    public int compareTo(@NotNull Object o)
    {
        if ((o instanceof FileSystemObjectRef))
        {
            FileSystemObjectRef that = (FileSystemObjectRef) o;
            int result = getConnectionId().compareTo(that.getConnectionId());
            if (result != 0)
            {
                return result;
            }
            if ((this.parent != null) && (that.parent != null))
            {
                if (this.parent.equals(that.parent))
                {
                    result = this.objectType.compareTo(that.objectType);
                    if (result != 0)
                    {
                        return result;
                    }
                    int nameCompare = this.objectName.compareTo(that.objectName);
                    return nameCompare == 0 ? this.overload - that.overload : nameCompare;
                }
                return this.parent.compareTo(that.parent);
            }
            if ((this.parent == null) && (that.parent == null))
            {
                result = this.objectType.compareTo(that.objectType);
                if (result != 0)
                {
                    return result;
                }
                return this.objectName.compareTo(that.objectName);
            }
            if (this.parent == null)
            {
                return -1;
            }
            if (that.parent == null)
            {
                return 1;
            }
        }
        return 0;
    }

    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if ((o == null) || (getClass() != o.getClass()))
        {
            return false;
        }
        FileSystemObjectRef that = (FileSystemObjectRef) o;
        return hashCode() == that.hashCode();
    }

    public String toString()
    {
        return getObjectName();
    }

    public int hashCode()
    {
        if (this.hashCode == -1)
        {
            this.hashCode = (getConnectionId() + '#' + serialize()).hashCode();
        }
        return this.hashCode;
    }

    public String getObjectName()
    {
        return this.objectName;
    }

    public FileSystemObjectType getObjectType()
    {
        return this.objectType;
    }
}
