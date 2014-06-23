/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.codeInsight.daemon.impl.analysis.HighlightVisitorImpl;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaResolveResult;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReferenceParameterList;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GosuPropertyMemberAccessExpressionImpl extends GosuFieldAccessExpressionImpl implements PsiCallExpression {

  public GosuPropertyMemberAccessExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl) ) {
      ((JavaElementVisitor)visitor).visitCallExpression( this );
    } else if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitFieldAccessExpression(this);
    } else {
      visitor.visitElement( this );
    }
  }

  @Nullable
  @Override
  public PsiExpressionList getArgumentList() {
    return (GosuExpressionListImpl)findChildByType( GosuElementTypes.ELEM_TYPE_ArgumentListClause );
  }

  @Override
  public PsiMethod resolveMethod() {
    PsiElement target = resolve();
    return target instanceof PsiMethod ? (PsiMethod)target : null;
  }

  @NotNull
  @Override
  public JavaResolveResult resolveMethodGenerics() {
    throw new UnsupportedOperationException( "Not implemented yet" );
  }

  @NotNull
  @Override
  public PsiReferenceParameterList getTypeArgumentList() {
    return null;
  }
}
