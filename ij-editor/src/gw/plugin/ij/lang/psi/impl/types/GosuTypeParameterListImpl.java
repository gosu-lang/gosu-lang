/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.types;

import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.expressions.ITypeParameterListClause;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;

public class GosuTypeParameterListImpl extends GosuPsiElementImpl<ITypeParameterListClause> implements IGosuTypeParameterList {
  public GosuTypeParameterListImpl(@NotNull GosuCompositeElement node) {
    super(node);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitTypeParameterList(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
