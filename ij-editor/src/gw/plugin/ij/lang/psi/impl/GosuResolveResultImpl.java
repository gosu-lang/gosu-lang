/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiSubstitutor;
import gw.lang.reflect.IFeatureInfo;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import org.jetbrains.annotations.Nullable;

public class GosuResolveResultImpl implements IGosuResolveResult {

  private final PsiElement myElement;
  private final boolean myIsAccessible;
  private final boolean myIsStaticsOK;
  private final PsiSubstitutor mySubstitutor;
  private final IGosuPsiElement myCurrentFileResolveContext;
  private final IFeatureInfo myFeatureInfo;

  public GosuResolveResultImpl(PsiElement element, boolean isAccessible, IFeatureInfo myFeatureInfo) {
    this(element, null, PsiSubstitutor.EMPTY, isAccessible, true, myFeatureInfo);
  }

  public GosuResolveResultImpl(PsiElement element,
                               IGosuPsiElement context,
                               PsiSubstitutor substitutor,
                               boolean isAccessible,
                               boolean staticsOK,
                               IFeatureInfo featureInfo) {
    myCurrentFileResolveContext = context;
    myElement = element; //element instanceof PsiClass ? GosuClassSubstitutor.getSubstitutedClass( (PsiClass)element ) : element;
    myIsAccessible = isAccessible;
    mySubstitutor = substitutor;
    myIsStaticsOK = staticsOK;
    myFeatureInfo = featureInfo;
  }

    public GosuResolveResultImpl(PsiElement element,
                               IGosuPsiElement context,
                               PsiSubstitutor substitutor,
                               boolean isAccessible,
                               boolean staticsOK) {
      this(element, context, substitutor, isAccessible, staticsOK, null);
  }

  public PsiSubstitutor getSubstitutor() {
    return mySubstitutor;
  }

  public boolean isPackagePrefixPackageReference() {
    return false;
  }

  public boolean isAccessible() {
    return myIsAccessible;
  }

  @Override
  public boolean isStaticsOK() {
    return myIsStaticsOK;
  }

  public boolean isStaticsScopeCorrect() {
    return myIsStaticsOK;
  }

  @Nullable
  public PsiElement getCurrentFileResolveScope() {
    return null;
  }

  @Nullable
  public PsiElement getElement() {
    return myElement;
  }

  public boolean isValidResult() {
    return isAccessible();
  }

  public boolean equals(@Nullable Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    GosuResolveResultImpl that = (GosuResolveResultImpl) o;

    PsiManager manager = myElement == null ? null : myElement.getManager();
    return myIsAccessible == that.myIsAccessible && manager != null && manager.areElementsEquivalent(myElement, that.myElement);
  }

  public int hashCode() {
    int result = 0;
    if (myElement instanceof PsiNamedElement) {
      String name = ((PsiNamedElement) myElement).getName();
      if (name != null) {
        result = name.hashCode();
      }
    }
    result = 31 * result + (myIsAccessible ? 1 : 0);
    return result;
  }

  public IGosuPsiElement getCurrentFileResolveContext() {
    return myCurrentFileResolveContext;
  }

  @Override
  public IFeatureInfo getFeatureInfo() {
    return myFeatureInfo;
  }
}
