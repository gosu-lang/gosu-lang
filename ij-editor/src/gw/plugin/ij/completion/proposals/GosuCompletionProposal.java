/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import com.intellij.psi.PsiElement;
import gw.lang.reflect.IFeatureInfo;
import org.jetbrains.annotations.Nullable;

public abstract class GosuCompletionProposal {
  private int _weight = 0;

  public int getWeight() {
    return _weight;
  }

  public void setWeight(int weight) {
    _weight = weight;
  }

  @Nullable
  public abstract PsiElement resolve(PsiElement context);

  @Nullable
  public abstract IFeatureInfo getFeatureInfo();

  public abstract String getGenericName();
}
