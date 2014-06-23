/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api;

import com.intellij.psi.JavaResolveResult;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiSubstitutor;
import gw.lang.reflect.IFeatureInfo;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.Nullable;

public interface IGosuResolveResult extends JavaResolveResult {
  IGosuResolveResult[] EMPTY_ARRAY = new IGosuResolveResult[0];

  boolean isAccessible();

  boolean isStaticsOK();

  @Nullable
  IGosuPsiElement getCurrentFileResolveContext();

  PsiSubstitutor getSubstitutor();

  IFeatureInfo getFeatureInfo();

  @Nullable
  public static final IGosuResolveResult EMPTY_RESULT = new IGosuResolveResult() {
    public boolean isAccessible() {
      return false;
    }

    @Override
    public boolean isStaticsScopeCorrect() {
      return false;
    }

    @Nullable
    @Override
    public PsiElement getCurrentFileResolveScope() {
      return null;
    }

    public IGosuPsiElement getCurrentFileResolveContext() {
      return null;
    }

    public boolean isStaticsOK() {
      return true;
    }

    public PsiSubstitutor getSubstitutor() {
      return PsiSubstitutor.EMPTY;
    }

    @Override
    public IFeatureInfo getFeatureInfo() {
      return null;
    }

    @Override
    public boolean isPackagePrefixPackageReference() {
      return false;
    }

    @Nullable
    public PsiElement getElement() {
      return null;
    }

    public boolean isValidResult() {
      return false;
    }
  };
}
