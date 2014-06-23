/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiType;
import gw.lang.parser.IExpansionPropertyInfo;
import gw.lang.parser.expressions.IMemberExpansionExpression;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IPropertyInfoDelegate;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GosuMemberExpansionExpressionImpl extends GosuReferenceExpressionImpl<IMemberExpansionExpression> implements IGosuCodeReferenceElement, IGosuTypeElement /*, PsiCallExpression*/ {
  public GosuMemberExpansionExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    return findLastChildByType(GosuTokenTypes.TT_IDENTIFIER);
  }

  @Override
  public IGosuCodeReferenceElement getQualifier() {
    return getFirstChild() instanceof IGosuCodeReferenceElement ? (IGosuCodeReferenceElement) getFirstChild() : null;
  }

  @Override
  public void setQualifier(IGosuCodeReferenceElement newQualifier) {
    throw new UnsupportedOperationException("Men at work");
  }

  @Nullable
  public IGosuTypeParameterList getTypeParameterList() {
    return null;
  }

  @Override
  public PsiType[] getTypeArguments() {
    return PsiType.EMPTY_ARRAY;
  }

  @Override
  public PsiElement resolve() {
    return ExecutionUtil.execute(new SafeCallable<PsiElement>(this) {
      @Nullable
      public PsiElement execute() throws Exception {
        final IPropertyInfo pi = getPropertyInfo();
        return pi != null ? PsiFeatureResolver.resolveProperty(pi, GosuMemberExpansionExpressionImpl.this) : null;
      }
    });
  }

  @Nullable
  private IPropertyInfo getPropertyInfo() {
    try {
      IPropertyInfo pi = getParsedElement().getPropertyInfo();
      while (pi instanceof IPropertyInfoDelegate) {
        pi = ((IPropertyInfoDelegate) pi).getSource();
      }
      while (pi instanceof IExpansionPropertyInfo) {
        pi = ((IExpansionPropertyInfo) pi).getDelegate();
      }
      return pi;

    } catch (Exception e) {
      // No property exists, pe is errant
      return null;
    }
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitMemberExpansionExpression(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
