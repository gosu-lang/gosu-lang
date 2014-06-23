/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.view;

import com.intellij.lang.Language;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiReference;
import com.intellij.psi.SingleRootFileViewProvider;
import com.intellij.psi.impl.source.resolve.reference.impl.PsiMultiReference;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNewExpressionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuFileViewProvider extends SingleRootFileViewProvider {
  public GosuFileViewProvider(@NotNull PsiManager manager, @NotNull VirtualFile file) {
    super(manager, file);
  }

  public GosuFileViewProvider(@NotNull PsiManager manager, @NotNull VirtualFile virtualFile, boolean physical) {
    super(manager, virtualFile, physical);
  }

  protected GosuFileViewProvider(@NotNull PsiManager manager, @NotNull VirtualFile virtualFile, boolean physical, @NotNull Language language) {
    super(manager, virtualFile, physical, language);
  }

  @Override
  public PsiReference findReferenceAt(int offset) {
    final PsiReference ref = super.findReferenceAt(offset);
    return findRef(ref, offset);
  }

  @Override
  public PsiReference findReferenceAt(int offset, @NotNull Language language) {
    final PsiReference ref = super.findReferenceAt(offset, language);
    if (language instanceof GosuLanguage) {
      return findRef(ref, offset);
    }
    return ref;
  }

  public boolean isPhysical() {
    return super.isPhysical() || getVirtualFile().getName().startsWith(GosuScratchpadFileImpl.GOSU_SCRATCHPAD_NAME);
  }

  private PsiReference findRef(@Nullable PsiReference psiReference, int offset) {
    // Handle resolving constructor inside new-expression, instead of resolving the type-literal
    if (psiReference instanceof PsiMultiReference) {
      PsiReference[] refs = ((PsiMultiReference) psiReference).getReferences();
      for (PsiReference ref : refs) {
        if (ref instanceof GosuNewExpressionImpl) {
          return ref;
        }
      }

      for (PsiReference ref : refs) {
        if (!(ref instanceof PsiElement)) {
          return ref;
        }
        if (((PsiElement) ref).getTextRange().containsOffset(offset)) {
          return psiReference;
        }
      }
    }
    return psiReference;
  }
}
