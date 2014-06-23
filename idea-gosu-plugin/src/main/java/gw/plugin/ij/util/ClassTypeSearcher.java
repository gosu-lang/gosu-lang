/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.psi.*;

import java.util.HashSet;
import java.util.Set;

public class ClassTypeSearcher extends PsiTypeVisitor<Boolean> {
  private final Set<PsiClassType> typeParams = new HashSet<>();

  public Boolean visitArrayType(final PsiArrayType arrayType) {
    return arrayType.getComponentType().accept(this);
  }

  public Boolean visitClassType(final PsiClassType classType) {
    typeParams.add(classType);
    final PsiType[] types = classType.getParameters();
    for (final PsiType psiType : types) {
      if (psiType.accept(this)) return true;
    }
    return false;
  }

  public Boolean visitWildcardType(final PsiWildcardType wildcardType) {
    final PsiType bound = wildcardType.getBound();
    return bound != null && bound.accept(this).booleanValue();
  }

  public Set<PsiClassType> getClasses() {
    return typeParams;
  }
}