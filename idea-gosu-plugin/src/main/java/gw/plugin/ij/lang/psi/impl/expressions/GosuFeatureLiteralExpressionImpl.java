/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiType;
import gw.lang.parser.IExpansionPropertyInfo;
import gw.lang.parser.expressions.IFeatureLiteralExpression;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IHasParameterInfos;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IMethodInfoDelegate;
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


public class GosuFeatureLiteralExpressionImpl extends GosuReferenceExpressionImpl<IFeatureLiteralExpression> implements IGosuCodeReferenceElement, IGosuTypeElement {
  public GosuFeatureLiteralExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    return findLastChildByType(GosuTokenTypes.TT_IDENTIFIER);
  }

  @Override
  public IGosuCodeReferenceElement getQualifier() {
    final PsiElement firstChild = getFirstChild();
    return firstChild instanceof IGosuCodeReferenceElement ? (IGosuCodeReferenceElement) firstChild : null;
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
      public PsiElement execute() throws Exception {
        IFeatureInfo feature = getParsedElement().getFeature();
        if (feature == null) {
          return null;
        } else if (feature instanceof IPropertyInfo) {
          return PsiFeatureResolver.resolveProperty((IPropertyInfo) getRootFeatureInfo(feature), GosuFeatureLiteralExpressionImpl.this);
        } else if (feature instanceof IMethodInfo || feature instanceof IConstructorInfo) {
          return PsiFeatureResolver.resolveMethodOrConstructor((IHasParameterInfos) getRootFeatureInfo(feature), GosuFeatureLiteralExpressionImpl.this);
        }
        throw new AssertionError();
      }
    });
  }

  @Nullable
  private IFeatureInfo getRootFeatureInfo(IFeatureInfo feature) {
    try {
      while (feature instanceof IPropertyInfoDelegate) {
        feature = ((IPropertyInfoDelegate) feature).getSource();
      }
      while (feature instanceof IExpansionPropertyInfo) {
        feature = ((IExpansionPropertyInfo) feature).getDelegate();
      }
      while (feature instanceof IMethodInfoDelegate) {
        feature = ((IMethodInfoDelegate) feature).getSource();
      }
      return feature;
    } catch (Exception e) {
      return null; // No property exists, pe is errant
    }
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitFeatureLiteralExpression(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
