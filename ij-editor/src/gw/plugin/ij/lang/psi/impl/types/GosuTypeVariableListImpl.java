/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.types;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiTypeParameter;
import gw.lang.parser.expressions.ITypeVariableListClause;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariable;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeVariableList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 */
public class GosuTypeVariableListImpl extends GosuPsiElementImpl<ITypeVariableListClause> implements IGosuTypeVariableList {
  public GosuTypeVariableListImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  @NotNull
  public String toString() {
    return "Type variable list";
  }

  @NotNull
  public IGosuTypeVariable[] getTypeParameters() {
    return findChildrenByClass(IGosuTypeVariable.class);
  }

  public int getTypeParameterIndex(PsiTypeParameter typeParameter) {
    final IGosuTypeVariable[] typeParameters = getTypeParameters();
    for (int i = 0; i < typeParameters.length; i++) {
      if (typeParameters[i].equals(typeParameter)) {
        return i;
      }
    }

    return -1;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitTypeVariableList(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
