/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.parser;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiType;
import gw.lang.parser.expressions.IBlockInvocation;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.Nullable;

public class GosuBlockInvocationImpl extends GosuReferenceExpressionImpl<IBlockInvocation> implements IGosuCodeReferenceElement, IGosuTypeElement {
  public GosuBlockInvocationImpl(GosuCompositeElement node) {
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
//            IMethodInfo methodInfo = null;
//            IBlockInvocation parsedElement = getParsedElement();
//            IFunctionType functionType = parsedElement.getFunctionType();
//            if (functionType != null) {
//              methodInfo = functionType.getMethodInfo();
//            }
//            if (methodInfo == null) {
//              IFunctionSymbol functionSymbol = parsedElement.getFunctionSymbol();
//              if (functionSymbol instanceof IDynamicFunctionSymbol) {
//                IAttributedFeatureInfo methodOrConstructorInfo = ((IDynamicFunctionSymbol) functionSymbol).getMethodOrConstructorInfo(true);
//                if (methodOrConstructorInfo instanceof IMethodInfo) {
//                  methodInfo = (IMethodInfo) methodOrConstructorInfo;
//                }
//              }
//            }
//            if (methodInfo != null) {
//              return resolveMethod(methodInfo);
//            } else {
//              return null;
//            }
        return null;
      }
    });
  }
}
