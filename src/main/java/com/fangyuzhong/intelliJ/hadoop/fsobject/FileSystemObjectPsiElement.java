package com.fangyuzhong.intelliJ.hadoop.fsobject;

import com.fangyuzhong.intelliJ.hadoop.core.dispose.DisposableBase;
import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.navigation.ItemPresentation;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.search.EverythingGlobalScope;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by fangyuzhong on 17-7-16.
 */
public class FileSystemObjectPsiElement
        extends DisposableBase
        implements PsiNamedElement, NavigationItem
{
    private static PsiFile DUMMY_FILE;
    private FileSystemObjectRef objectRef;

    public FileSystemObjectPsiElement(FileSystemObjectRef objectRef)
    {
        this.objectRef = objectRef;
    }

    @Nullable
    public String getName()
    {
        return this.objectRef.getObjectName();
    }

    @Nullable
    public ItemPresentation getPresentation()
    {
        return getObject().getPresentation();
    }

    public final PsiElement setName(@NonNls @NotNull String name)
    {
        throw new IncorrectOperationException("Operation not supported");
    }

    public PsiManager getManager()
    {
        return PsiManager.getInstance(getProject());
    }

    @NotNull
    public PsiElement[] getChildren()
    {
        return PsiElement.EMPTY_ARRAY;
    }

    public PsiElement getParent()
    {
        return null;
    }

    public PsiElement getFirstChild()
    {
        return null;
    }

    public PsiElement getLastChild()
    {
        return null;
    }

    public PsiElement getNextSibling()
    {
        return null;
    }

    public PsiElement getPrevSibling()
    {
        return null;
    }

    public PsiElement findElementAt(int offset)
    {
        return null;
    }

    public PsiReference findReferenceAt(int offset)
    {
        return null;
    }

    public PsiFile getContainingFile()
            throws PsiInvalidElementAccessException
    {
       return  null;
    }

    public PsiElement getOriginalElement()
    {
        return this;
    }

    public boolean textMatches(@NotNull CharSequence text)
    {
        return false;
    }

    public boolean textMatches(@NotNull PsiElement element)
    {
        return false;
    }

    public boolean textContains(char c)
    {
        return false;
    }

    public void accept(@NotNull PsiElementVisitor visitor)
    {
    }

    public PsiElement getNavigationElement()
    {
        return this;
    }

    public int getStartOffsetInParent()
    {
        return 0;
    }

    public int getTextOffset()
    {
        return 0;
    }

    public void acceptChildren(@NotNull PsiElementVisitor visitor)
    {
    }

    public PsiElement copy()
    {
        return this;
    }

    public PsiElement add(@NotNull PsiElement element)
    {
        return null;
    }

    public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor)
    {
        return null;
    }

    public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor)
    {
        return null;
    }

    public void checkAdd(@NotNull PsiElement element)
    {
    }

    public PsiElement addRange(PsiElement first, PsiElement last)
    {
        return null;
    }

    public PsiElement addRangeBefore(@NotNull PsiElement first, @NotNull PsiElement last, PsiElement anchor)
    {
        return null;
    }

    public PsiElement addRangeAfter(PsiElement first, PsiElement last, PsiElement anchor)
    {
        return null;
    }

    public void delete()
    {
    }

    public void checkDelete()
    {
    }

    public void deleteChildRange(PsiElement first, PsiElement last)
    {
    }

    public PsiElement replace(@NotNull PsiElement newElement)
    {
        return null;
    }

    public boolean isValid()
    {
        return true;
    }

    public boolean isWritable()
    {
        return false;
    }

    public PsiReference getReference()
    {
        return null;
    }

    @NotNull
    public PsiReference[] getReferences()
    {
        return new PsiReference[0];
    }

    public PsiElement getContext()
    {
        return null;
    }

    public boolean isPhysical()
    {
        return false;
    }

    @NotNull
    public GlobalSearchScope getResolveScope()
    {
        return null;
    }

    @NotNull
    public SearchScope getUseScope()
    {
        return new EverythingGlobalScope();
    }

    public ASTNode getNode()
    {
        return null;
    }

    public boolean processDeclarations(@NotNull PsiScopeProcessor psiScopeProcessor, @NotNull ResolveState resolveState, @Nullable PsiElement psiElement, @NotNull PsiElement psiElement1)
    {

        return false;
    }

    public <T> T getCopyableUserData(Key<T> key)
    {
        return null;
    }

    public <T> void putCopyableUserData(Key<T> key, T value)
    {
    }

    public <T> T getUserData(@NotNull Key<T> key)
    {

        return null;
    }

    public <T> void putUserData(@NotNull Key<T> key, T value)
    {

    }

    public boolean isEquivalentTo(PsiElement psiElement)
    {
        return false;
    }

    @NotNull
    public Project getProject()
            throws PsiInvalidElementAccessException
    {
        return getObject().getProject();
    }

    @NotNull
    public Language getLanguage()
    {
       return null;
    }

    public TextRange getTextRange()
    {
        return new TextRange(0, getText().length());
    }

    public int getTextLength()
    {
        return getText().length();
    }

    @NonNls
    public String getText()
    {
        return getName();
    }

    @NotNull
    public char[] textToCharArray()
    {
        return  getText().toCharArray();
    }

    public void navigate(boolean requestFocus)
    {
        getObject().navigate(requestFocus);
    }

    public boolean canNavigate()
    {
        return true;
    }

    public boolean canNavigateToSource()
    {
        return false;
    }

    public Icon getIcon(int flags)
    {
        return getObject().getIcon();
    }

    @NotNull
    public FileSystemObject getObject()
    {
        return FileSystemObjectRef.getnn(this.objectRef);
    }
}

