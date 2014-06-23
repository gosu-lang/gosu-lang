/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api.statements.typedef;

import com.intellij.psi.NavigatablePsiElement;
import com.intellij.psi.PsiClass;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.util.ArrayFactory;
import gw.plugin.ij.lang.psi.IGosuNamedElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IGosuTypeDefinition extends NavigatablePsiElement, PsiClass, IGosuPsiElement, IGosuNamedElement {
  String DEFAULT_BASE_CLASS_NAME = "gosu.lang.GosuObject";

  IGosuTypeDefinition[] EMPTY_ARRAY = new IGosuTypeDefinition[0];

  ArrayFactory<IGosuTypeDefinition> ARRAY_FACTORY = new ArrayFactory<IGosuTypeDefinition>() {
    @NotNull
    public IGosuTypeDefinition[] create(int count) {
      return count == 0 ? EMPTY_ARRAY  : new IGosuTypeDefinition[count];
    }
  };

  String[] getSuperClassNames();

  boolean isAnonymous();

  boolean isEnhancement();

  @Nullable
  IGosuReferenceList getExtendsClause();

  @Nullable
  IGosuReferenceList getImplementsClause();

  LeafPsiElement getGosuLBrace();

  @Nullable
  LeafPsiElement getGosuRBrace();
}
