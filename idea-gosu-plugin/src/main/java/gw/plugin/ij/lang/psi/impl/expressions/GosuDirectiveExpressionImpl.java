/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiElementVisitor;
import gw.lang.parser.expressions.IDirectiveExpression;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.statements.IGosuParametersOwner;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import gw.plugin.ij.lang.psi.impl.statements.params.GosuParameterListImpl;
import org.jetbrains.annotations.NotNull;

public class GosuDirectiveExpressionImpl extends GosuPsiElementImpl<IDirectiveExpression> implements IGosuParametersOwner {
  public GosuDirectiveExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @NotNull
  @Override
  public IGosuParameter[] getParameters() {
    final GosuParameterListImpl parameters = findChildByClass(GosuParameterListImpl.class);
    return parameters != null ? parameters.getParameters() : IGosuParameter.EMPTY_ARRAY;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitDirectiveExpression(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
