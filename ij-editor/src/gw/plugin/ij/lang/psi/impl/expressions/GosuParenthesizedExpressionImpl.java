/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiType;
import gw.lang.parser.expressions.IParenthesizedExpression;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;

public class GosuParenthesizedExpressionImpl extends GosuPsiElementImpl<IParenthesizedExpression> implements IGosuExpression {
  public GosuParenthesizedExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Override
  public PsiType getType() {
    return createType(getParsedElement().getType());
  }

  @NotNull
  public IGosuPsiElement getExpression() {
    return (IGosuPsiElement) getChildren()[0];
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitParenthesizedExpression(this);
    }
    else {
      visitor.visitElement( this );
    }
  }

}
