/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiType;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuIdentifierExpressionImpl extends GosuReferenceExpressionImpl<IIdentifierExpression> implements IGosuCodeReferenceElement, IGosuTypeElement {
  public GosuIdentifierExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    // Return the psi identifier *token*
    PsiElement child = this;
    while (child.getFirstChild() != null) {
      child = child.getFirstChild();
    }
    return child;
  }

  @Override
  public IGosuCodeReferenceElement getQualifier() {
    return null;
  }

  @Override
  public void setQualifier(IGosuCodeReferenceElement newQualifier) {
    throw new UnsupportedOperationException("Men at work");
  }

  @Override
  public PsiType[] getTypeArguments() {
    return PsiType.EMPTY_ARRAY;
  }

  @Nullable
  public IGosuTypeParameterList getTypeParameterList() {
    return null;
  }

  @Override
  public PsiElement resolve() {
    return ExecutionUtil.execute(new SafeCallable<PsiElement>(this) {
      @Nullable
      public PsiElement execute() throws Exception {
        IParsedElement pe = getParsedElement();
        if (pe == null) {
          return null;
        }

        if (pe instanceof ITypeLiteralExpression) {
          IMetaType metaType = ((ITypeLiteralExpression) pe).getType();
          return PsiTypeResolver.resolveType(metaType.getType(), GosuIdentifierExpressionImpl.this);
        }

        if (pe instanceof IIdentifierExpression) {
          IIdentifierExpression parsedElement = (IIdentifierExpression) pe;
          ISymbol symbol = parsedElement.getSymbol();
          PsiElement element = PsiFeatureResolver.resolveSymbol(symbol, symbol.getGosuClass(), GosuIdentifierExpressionImpl.this);
          if (element == null) {
            IGosuClass gsClass = parsedElement.getGosuClass();
            if (symbol.isValueBoxed() && gsClass.isAnonymous()) {
              gsClass = (IGosuClass) gsClass.getEnclosingType();
            }
            element = PsiFeatureResolver.resolveSymbol(symbol, gsClass, GosuIdentifierExpressionImpl.this);
          }
          return element;
        }

        return null;
      }
    });
  }

  @NotNull
  public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
    PsiElement elt = GosuTypeLiteralImpl.bindType(this, element, (AbstractGosuClassFileImpl) getContainingFile());
    return elt != null ? elt : this;
  }


  @Nullable
  public static IType maybeUnwrapProxy(@Nullable IType type) {
    if (type != null && type.isParameterizedType()) {
      type = type.getGenericType();
    }
    return type != null ? IGosuClass.ProxyUtil.getProxiedType(type) : null;
  }

  @Override
  public String toString() {
    return getText();
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitIdentifierExpression(this);
    }
    else {
      visitor.visitElement( this );
    }
  }
}
