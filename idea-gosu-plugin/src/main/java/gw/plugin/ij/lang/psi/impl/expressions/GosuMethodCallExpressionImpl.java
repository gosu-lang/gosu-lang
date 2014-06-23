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
import com.intellij.psi.PsiType;
import gw.lang.parser.IConstructorFunctionSymbol;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IHasParameterInfos;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.plugin.ij.lang.GosuTokenImpl;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GosuMethodCallExpressionImpl extends GosuReferenceExpressionImpl<IMethodCallExpression>
    implements IGosuCodeReferenceElement, IGosuTypeElement, PsiCallExpression {

  public GosuMethodCallExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    PsiElement child = findLastChildByType( GosuTokenTypes.TT_IDENTIFIER );
    if( child == null ) {
       // Could be a non-reserved keyword
       child = getFirstChild();
       if( child instanceof GosuTokenImpl ) {
         return child;
       }
     }
     return child;
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

  @NotNull
  @Override
  public PsiReferenceParameterList getTypeArgumentList() {
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
        final IMethodCallExpression parsedElement = getParsedElement();
        if (parsedElement == null) {
          return null;
        }

        IHasParameterInfos featureInfo = null;
        final IFunctionType functionType = parsedElement.getFunctionType();
        if (functionType != null) {
          featureInfo = (IHasParameterInfos) functionType.getMethodOrConstructorInfo();
        }

        final IFunctionSymbol functionSymbol = parsedElement.getFunctionSymbol();
        if (featureInfo == null) {
          if (functionSymbol instanceof IConstructorFunctionSymbol) {
            featureInfo = ((IConstructorFunctionSymbol) functionSymbol).getConstructorInfo();
          } else if (functionSymbol instanceof IDynamicFunctionSymbol) {
            featureInfo = (IHasParameterInfos) ((IDynamicFunctionSymbol) functionSymbol).getMethodOrConstructorInfo(true);
          }
        }

        if (featureInfo != null) {
          final IHasParameterInfos info = resolveParamerizedFeatureInfo(featureInfo);
          return PsiFeatureResolver.resolveMethodOrConstructor(info, GosuMethodCallExpressionImpl.this);
        } else {
          return functionSymbol != null ? PsiFeatureResolver.resolveMethodOrConstructor(functionSymbol, GosuMethodCallExpressionImpl.this) : null;
        }
      }
    });
  }

  @NotNull
  private IHasParameterInfos resolveParamerizedFeatureInfo(@NotNull IHasParameterInfos featureInfo) {
    if (featureInfo.getOwnersType().isParameterizedType() && featureInfo instanceof IGosuMethodInfo) {
      final IReducedDynamicFunctionSymbol dfs = ((IGosuMethodInfo) featureInfo).getDfs();
      if (dfs != null) {
        final IReducedDynamicFunctionSymbol backingDfs = dfs.getBackingDfs();
        if (backingDfs != null) {
          final IAttributedFeatureInfo resolvedFeatureInfo = backingDfs.getMethodOrConstructorInfo();
          if (resolvedFeatureInfo instanceof IHasParameterInfos) {
            featureInfo = (IHasParameterInfos) resolvedFeatureInfo;
          }
        }
      }
    }
    return featureInfo;
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl) ) {
      ((JavaElementVisitor)visitor).visitCallExpression(this);
    } else if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitMethodCallExpression(this);
    }
    else {
      visitor.visitElement( this );
    }

  }

  @NotNull
  @Override
  public PsiExpressionList getArgumentList() {
    return (GosuExpressionListImpl)findChildByType( GosuElementTypes.ELEM_TYPE_ArgumentListClause );
  }

  @Override
  public PsiMethod resolveMethod() {
    PsiElement ref = resolve();
    return ref instanceof PsiMethod ? (PsiMethod)ref : null;
  }

  @NotNull
  @Override
  public JavaResolveResult resolveMethodGenerics() {
    throw new UnsupportedOperationException( "Not implemented yet" );
  }
}
