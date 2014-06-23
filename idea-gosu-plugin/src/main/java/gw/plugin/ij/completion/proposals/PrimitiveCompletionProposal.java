/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import gw.lang.reflect.IFeatureInfo;
import org.jetbrains.annotations.Nullable;

public class PrimitiveCompletionProposal extends GosuCompletionProposal {
  private final PsiType _type;

  public PrimitiveCompletionProposal(PsiType primitiveType) {
    _type = primitiveType;
  }

  @Nullable
  @Override
  public PsiElement resolve(PsiElement context) {
    return null;
  }

  @Nullable
  @Override
  public IFeatureInfo getFeatureInfo() {
    return null;
  }

  @Override
  public String getGenericName() {
    return _type.getCanonicalText();
  }

  public PsiType getPrimitiveType() {
    return _type;
  }
}