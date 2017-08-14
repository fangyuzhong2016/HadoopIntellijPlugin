package com.fangyuzhong.intelliJ.hadoop.core.content;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.ContentDependencyAdapter;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.VoidContentDependencyAdapter;
import com.fangyuzhong.intelliJ.hadoop.core.content.loader.DynamicContentLoadException;
import com.fangyuzhong.intelliJ.hadoop.core.content.loader.DynamicContentLoader;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposerUtil;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.FailsafeUtil;
import com.fangyuzhong.intelliJ.hadoop.core.list.AbstractFiltrableList;
import com.fangyuzhong.intelliJ.hadoop.core.list.FiltrableList;
import com.fangyuzhong.intelliJ.hadoop.core.thread.BackgroundTask;
import com.fangyuzhong.intelliJ.hadoop.core.util.CollectionUtil;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.util.Disposer;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by fangyuzhong on 17-7-15.
 */
public abstract class DynamicContentImpl<T extends DynamicContentElement>
        extends DisposableBase
        implements DynamicContent<T>
{
    public static final List EMPTY_CONTENT = Collections.unmodifiableList(new ArrayList(0));
    public static final List EMPTY_UNTOUCHED_CONTENT = Collections.unmodifiableList(new ArrayList(0));
    private long changeTimestamp = 0L;
    private volatile boolean loading = false;
    private volatile boolean loadingInBackground = false;
    private volatile boolean loaded = false;
    private volatile boolean dirty = false;
    private GenericFileSystemElement parent;
    protected DynamicContentLoader<T> loader;
    protected ContentDependencyAdapter dependencyAdapter;
    private boolean indexed;
    private Map<String, T> index;
    protected List<T> elements = EMPTY_UNTOUCHED_CONTENT;

    protected DynamicContentImpl(@NotNull GenericFileSystemElement parent, @NotNull DynamicContentLoader<T> loader, ContentDependencyAdapter dependencyAdapter, boolean indexed)
    {
        this.parent = parent;
        this.loader = loader;
        this.dependencyAdapter = dependencyAdapter;
        this.indexed = indexed;
    }

    @Nullable
    public GenericFileSystemElement getParentElement()
    {
        return (GenericFileSystemElement) FailsafeUtil.get(this.parent);
    }

    @NotNull
    public ConnectionHandler getConnectionHandler()
    {
        ConnectionHandler tmp15_12 = ((ConnectionHandler) FailsafeUtil.get(getParentElement().getConnectionHandler()));
        if (tmp15_12 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/DynamicContentImpl", "getConnectionHandler"}));
        }
        return tmp15_12;
    }

    public DynamicContentLoader<T> getLoader()
    {
        return this.loader;
    }

    public ContentDependencyAdapter getDependencyAdapter()
    {
        return this.dependencyAdapter;
    }

    public long getChangeTimestamp()
    {
        return this.changeTimestamp;
    }

    public boolean isLoaded()
    {
        return this.loaded;
    }

    public boolean canLoadFast()
    {
        return this.dependencyAdapter.canLoadFast();
    }

    public boolean isSubContent()
    {
        return this.dependencyAdapter.isSubContent();
    }

    public boolean isLoading()
    {
        return this.loading;
    }

    public boolean isDirty()
    {
        return (this.dirty) || (this.dependencyAdapter.isDirty());
    }

    public void markDirty()
    {
        this.dirty = true;
        ContentDependencyAdapter dependencyAdapter = getDependencyAdapter();
        dependencyAdapter.markSourcesDirty();
    }

    private boolean shouldReload()
    {
        return (!isDisposed()) && (this.loaded) && (!this.loading);
    }

    private boolean shouldRefresh()
    {
        return (!isDisposed()) && (!this.loading);
    }

    public final void load(boolean force)
    {
        if (shouldLoad(force))
        {
            synchronized (this)
            {
                if (shouldLoad(force))
                {
                    this.loading = true;
                    try
                    {
                        performLoad();
                        this.loaded = true;
                    } catch (InterruptedException e)
                    {
                        setElements(EMPTY_CONTENT);
                        this.dirty = true;
                    } finally
                    {
                        this.loading = false;
                        updateChangeTimestamp();
                    }
                }
            }
        }
    }

    public final void reload()
    {
        if (shouldReload())
        {
            synchronized (this)
            {
                if (shouldReload())
                {
                    this.loading = true;
                    try
                    {
                        performReload();
                        List<T> elements = getAllElements();
                        for (T element : elements)
                        {
                            checkDisposed();
                           // element.refresh();
                        }
                        this.loaded = true;
                    } catch (InterruptedException e)
                    {
                        setElements(EMPTY_CONTENT);
                        this.dirty = true;
                    } finally
                    {
                        this.loading = false;
                        updateChangeTimestamp();
                    }
                }
            }
        }
    }

    public void refresh()
    {
        if (shouldRefresh())
        {
            synchronized (this)
            {
                if (shouldRefresh())
                {
                    markDirty();
                }
            }
        }
    }

    public final void loadInBackground(final boolean force)
    {
        if (shouldLoadInBackground(force))
        {
            synchronized (this)
            {
                if (shouldLoadInBackground(force))
                {
                    this.loadingInBackground = true;
                    ConnectionHandler connectionHandler = getConnectionHandler();
                    String connectionString = " (" + connectionHandler.getName() + ')';
                    new BackgroundTask(getProject(), "Loading data dictionary" + connectionString, true)
                    {
                        protected void execute(@NotNull ProgressIndicator progressIndicator)
                        {
                            if (progressIndicator == null)
                            {
                                throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", new Object[]{"progressIndicator", "com/dci/intellij/dbn/core/content/DynamicContentImpl$1", "execute"}));
                            }
                            try
                            {
                                DatabaseLoadMonitor.startBackgroundLoad();
                                DynamicContentImpl.this.load(force);
                            } finally
                            {
                                DatabaseLoadMonitor.endBackgroundLoad();
                                DynamicContentImpl.this.loadingInBackground = false;
                            }
                        }
                    }.start();
                }
            }
        }
    }

    boolean shouldLoadInBackground(boolean force)
    {
        return (!this.loadingInBackground) && (shouldLoad(force));
    }

    private void performLoad()
            throws InterruptedException
    {
        checkDisposed();
        this.dependencyAdapter.beforeLoad();
        checkDisposed();
        try
        {
            this.dirty = false;
            this.loader.loadContent(this, false);
        } catch (DynamicContentLoadException e)
        {
            this.dirty = (!e.isModelException());
        }
        checkDisposed();
        this.dependencyAdapter.afterLoad();
    }

    private void performReload()
            throws InterruptedException
    {
        checkDisposed();
        this.dependencyAdapter.beforeReload(this);
        checkDisposed();
        try
        {
            checkDisposed();
            this.loader.reloadContent(this);
        } catch (DynamicContentLoadException e)
        {
            this.dirty = (!e.isModelException());
        }
        checkDisposed();
        this.dependencyAdapter.afterReload(this);
    }

    public void updateChangeTimestamp()
    {
        this.changeTimestamp = System.currentTimeMillis();
    }

    public abstract void notifyChangeListeners();

    public void setElements(List<T> elements)
    {
        if ((isDisposed()) || (elements == null) || (elements.size() == 0))
        {
            elements = EMPTY_CONTENT;
            this.index = null;
        } else
        {
            sortElements(elements);
        }
        List<T> oldElements = this.elements;
        this.elements = new AbstractFiltrableList(elements)
        {
            @Nullable
            public Filter<T> getFilter()
            {
                return DynamicContentImpl.this.getFilter();
            }
        };
        updateIndex();
        if ((oldElements.size() != 0) || (elements.size() != 0))
        {
            notifyChangeListeners();
        }
        if ((!this.dependencyAdapter.isSubContent()) && (oldElements.size() > 0))
        {
            DisposerUtil.dispose(oldElements);
        }
    }

    public void sortElements(List<T> elements)
    {
        Collections.sort(elements);
    }

    @NotNull
    public List<T> getElements()
    {
        if (!isDisposed())
        {
            if ((isSubContent()) || (DatabaseLoadMonitor.isEnsureDataLoaded()) || (DatabaseLoadMonitor.isLoadingInBackground()))
            {
                load(false);
            } else
            {
                loadInBackground(false);
            }
        }
        List tmp53_50 = this.elements;
        if (tmp53_50 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/DynamicContentImpl", "getElements"}));
        }
        return tmp53_50;
    }

    public List getElementsNoLoad()
    {
        return this.elements;
    }

    @NotNull
    public List<T> getAllElements()
    {
        List<T> elements = getElements();
        if ((elements instanceof FiltrableList))
        {
            FiltrableList<T> filteredElements = (FiltrableList) elements;
            List
                    tmp23_18 = filteredElements.getFullList();
            if (tmp23_18 == null)
            {
                throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/DynamicContentImpl", "getAllElements"}));
            }
            return tmp23_18;
        }
        List<T> tmp60_59 = elements;
        if (tmp60_59 == null)
        {
            throw new IllegalStateException(String.format("@NotNull method %s.%s must not return null", new Object[]{"com/dci/intellij/dbn/core/content/DynamicContentImpl", "getAllElements"}));
        }
        return tmp60_59;
    }

    public List<T> getAllElementsNoLoad()
    {
        if ((this.elements instanceof FiltrableList))
        {
            FiltrableList<T> filteredElements = (FiltrableList) this.elements;
            return filteredElements.getFullList();
        }
        return this.elements;
    }

    protected void updateIndex()
    {
        if (this.indexed)
        {
            List<T> elements = this.elements;
            if ((elements instanceof FiltrableList))
            {
                elements = ((FiltrableList) elements).getFullList();
            }
            if (elements.size() > 30)
            {
                if (this.index == null)
                {
                    this.index = new THashMap();
                } else
                {
                    this.index.clear();
                }
                for (T element : elements)
                {
                    String name = element.getName().toUpperCase();
                    this.index.put(name, element);
                }
            } else
            {
                this.index = null;
            }
        }
    }

    public T getElement(String name, int overload)
    {
        if (name != null)
        {
            List<T> elements = getAllElements();
            if ((this.indexed) && (this.index != null))
            {
                return this.index.get(name.toUpperCase());
            }
            for (T element : elements)
            {
                if ((element.getName().equalsIgnoreCase(name)) && (
                        (overload == 0) || (overload == element.getOverload())))
                {
                    return element;
                }
            }
        }
        return null;
    }

    @Nullable
    public List<T> getElements(String name)
    {
        List<T> elements = null;
        for (T element : getAllElements())
        {
            if (element.getName().equalsIgnoreCase(name))
            {
                if (elements == null)
                {
                    elements = new ArrayList();
                }
                elements.add(element);
            }
        }
        return elements;
    }

    public int size()
    {
        return getElements().size();
    }

    public boolean shouldLoad(boolean force)
    {
        if ((this.loading) || (isDisposed()))
        {
            return false;
        }
        ConnectionHandler connectionHandler = getConnectionHandler();
        if ((force) || (!this.loaded))
        {
            return this.dependencyAdapter.canConnect(connectionHandler);
        }
        if (isDirty())
        {
            return this.dependencyAdapter.canLoad(connectionHandler);
        }
        return false;
    }

    public void checkDisposed()
            throws InterruptedException
    {
        if (isDisposed())
        {
            throw new InterruptedException();
        }
    }

    public void dispose()
    {
        if (!isDisposed())
        {
            super.dispose();
            if ((this.elements != EMPTY_CONTENT) && (this.elements != EMPTY_UNTOUCHED_CONTENT))
            {
                if (this.dependencyAdapter.isSubContent())
                {
                    this.elements.clear();
                } else
                {
                    DisposerUtil.dispose(this.elements);
                }
            }
            CollectionUtil.clearMap(this.index);
            Disposer.dispose(this.dependencyAdapter);
            this.dependencyAdapter = VoidContentDependencyAdapter.INSTANCE;
            this.parent = null;
        }
    }
}
