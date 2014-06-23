/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.codeInsight.generation;

import com.intellij.codeInsight.MethodImplementor;
import com.intellij.codeInsight.generation.GenerationInfo;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.util.IncorrectOperationException;
import gw.plugin.ij.lang.GosuLanguage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuMethodImplementor implements MethodImplementor {
  @NotNull
  @Override
  public PsiMethod[] getMethodsToImplement(@Nullable PsiClass aClass) {
    if(aClass == null || aClass.getLanguage() != GosuLanguage.instance()) {
      return new PsiMethod[0];
    }
    // XXX signature comparison is broken in caller, must be fixed before anything could be returned from here.
    return new PsiMethod[0];
  }

  @NotNull
  @Override
  public PsiMethod[] createImplementationPrototypes(@Nullable PsiClass inClass, PsiMethod method) throws IncorrectOperationException {
    if(inClass == null || inClass.getLanguage() != GosuLanguage.instance()) {
      return new PsiMethod[0];
    }
    // XXX Never called because PsiClass.getLBrace() isn't implemented in the Gosu psi tree and the caller hit's an NPE.
    return new PsiMethod[0];
  }

  @Override
  public boolean isBodyGenerated() {
    return false;
  }

  @Override
  public GenerationInfo createGenerationInfo(PsiMethod method, boolean mergeIfExists) {
    return null;
  }
}
