/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import com.intellij.psi.PsiElement;
import gw.lang.reflect.IFeatureInfo;
import org.jetbrains.annotations.Nullable;

public class RawCompletionProposal extends GosuCompletionProposal {
  private final String _rawCompletion;

  public RawCompletionProposal(String rawCompletion) {
    _rawCompletion = rawCompletion;
  }

  @Override
  public String toString() {
    return _rawCompletion;
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
    return _rawCompletion;
  }
}
