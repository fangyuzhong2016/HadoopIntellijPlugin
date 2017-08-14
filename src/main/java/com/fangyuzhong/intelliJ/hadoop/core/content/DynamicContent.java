package com.fangyuzhong.intelliJ.hadoop.core.content;

import com.fangyuzhong.intelliJ.hadoop.core.Filter;
import com.fangyuzhong.intelliJ.hadoop.core.content.dependency.ContentDependencyAdapter;
import com.fangyuzhong.intelliJ.hadoop.core.content.loader.DynamicContentLoader;
import com.fangyuzhong.intelliJ.hadoop.core.dispose.Disposable;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.ConnectionHandler;
import com.fangyuzhong.intelliJ.hadoop.fsconnection.GenericFileSystemElement;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by fangyuzhong on 17-7-15.
 */
 public interface DynamicContent<T extends DynamicContentElement>
        extends Disposable
{
     boolean shouldLoad(boolean paramBoolean);

     void load(boolean paramBoolean);

     void reload();

     void refresh();

     long getChangeTimestamp();

     boolean isLoaded();

     boolean isSubContent();

     boolean canLoadFast();

     boolean isLoading();

     boolean isDirty();

     boolean isDisposed();

     void markDirty();

     Project getProject();

     String getContentDescription();

    @NotNull
     List<T> getElements();

    @Nullable
     List<T> getElements(String paramString);

    @NotNull
     List<T> getAllElements();

    @Nullable
     Filter<T> getFilter();

     T getElement(String paramString, int paramInt);

     void setElements(@Nullable List<T> paramList);

     int size();

    @NotNull
    GenericFileSystemElement getParentElement();

     DynamicContentLoader getLoader();

     ContentDependencyAdapter getDependencyAdapter();

     ConnectionHandler getConnectionHandler();

     void loadInBackground(boolean paramBoolean);

     void updateChangeTimestamp();

     String getName();

     void checkDisposed()
            throws InterruptedException;
}
