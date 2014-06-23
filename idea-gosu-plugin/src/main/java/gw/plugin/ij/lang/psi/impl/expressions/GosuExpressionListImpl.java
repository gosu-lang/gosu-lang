/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpression;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiType;
import gw.lang.parser.expressions.IDirectiveExpression;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import org.jetbrains.annotations.NotNull;

public class GosuExpressionListImpl extends GosuPsiElementImpl<IDirectiveExpression> implements PsiExpressionList {
  public GosuExpressionListImpl(GosuCompositeElement node) {
    super(node);
  }

  @NotNull
  public PsiExpression[] getExpressions() {
    PsiElement[] children = getChildren();
    PsiExpression[] exprs = new PsiExpression[children.length];
    System.arraycopy(children, 0, exprs, 0, children.length);
    return exprs;
  }

  @NotNull
  public PsiType[] getExpressionTypes() {
    final PsiExpression[] expressions = getExpressions();
    final PsiType[] types = new PsiType[expressions.length];
    for (int i = 0; i < types.length; i++) {
      types[i] = expressions[i].getType();
    }
    return types;
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitExpressionList(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
