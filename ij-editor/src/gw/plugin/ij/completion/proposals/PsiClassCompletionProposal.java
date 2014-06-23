/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import com.google.common.base.Preconditions;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import gw.lang.reflect.IFeatureInfo;
import org.jetbrains.annotations.Nullable;

public class PsiClassCompletionProposal extends GosuCompletionProposal {
  private final PsiClass _class;

  public PsiClassCompletionProposal(PsiClass aClass, int weight) {
    _class = Preconditions.checkNotNull(aClass);
    setWeight(weight);
  }

  @Override
  public PsiElement resolve(PsiElement context) {
    return _class;
  }

  @Nullable
  @Override
  public IFeatureInfo getFeatureInfo() {
    return null;
  }

  @Override
  public String getGenericName() {
    return _class.getName();
  }

  public PsiClass getPsiClass() {
    return _class;
  }
}
