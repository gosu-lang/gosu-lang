/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiClassOwner;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiImportHolder;
import com.intellij.psi.PsiModifierListOwner;

public interface IGosuFileBase extends PsiFile, PsiClassOwner, PsiImportHolder, IGosuPsiElement, PsiModifierListOwner {
  PsiClass getPsiClass();
}
