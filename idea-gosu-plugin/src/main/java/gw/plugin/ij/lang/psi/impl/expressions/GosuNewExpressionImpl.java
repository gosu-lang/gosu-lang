/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.codeInsight.daemon.impl.analysis.HighlightVisitorImpl;
import com.intellij.psi.*;
import com.intellij.psi.impl.light.LightClassReferenceExpression;
import com.intellij.psi.impl.source.PsiClassReferenceType;
import com.intellij.psi.impl.source.PsiImmediateClassType;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.gs.ICompilableType;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.expressions.IGosuExpression;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.GosuPsiElementImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class GosuNewExpressionImpl extends GosuPsiElementImpl<INewExpression> implements IGosuExpression, PsiNewExpression, PsiModifierListOwner {
  public GosuNewExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Override
  public PsiType getType() {
    IType type = getTypeReferenced();
    if (type instanceof IErrorType) {
      final GosuTypeLiteralImpl typeLiteral = findChildByClass(GosuTypeLiteralImpl.class);
      if (typeLiteral != null) {
        return typeLiteral.getType();
      }
    } else if (type instanceof ICompilableType && ((ICompilableType) type).isAnonymous()) {
      IType superType = type.getSupertype();
      if (superType == null) {
        superType = type.getInterfaces()[0];
      }
      type = superType;
    }

    return createType(type);
  }

  @Nullable
  private IType getTypeReferenced() {
    final INewExpression element = getParsedElement();
    return element != null ? element.getType() : null;
  }

  @NotNull
  @Override
  public PsiType[] getTypeArguments() {
    return PsiType.EMPTY_ARRAY;
  }

  @Override
  public PsiMethod resolveConstructor() {
    PsiElement psiElement = ExecutionUtil.execute(new SafeCallable<PsiElement>(GosuNewExpressionImpl.this) {
      @Nullable
      public PsiElement execute() throws Exception {
        INewExpression newExpr = getParsedElement();
        if (newExpr == null) {
          return null;
        }
        IType newExprType = newExpr.getType();
        if (newExprType instanceof ITypeVariableType) {
          return PsiTypeResolver.resolveTypeVariable((ITypeVariableType) newExprType, GosuNewExpressionImpl.this);
        } else {
          IConstructorInfo ctor = newExpr.getConstructor();
          if (ctor != null && !newExpr.isAnonymousClass()) {
            return PsiFeatureResolver.resolveMethodOrConstructor(ctor, GosuNewExpressionImpl.this);
          } else {
            return null;
          }
        }
      }
    });

    return psiElement instanceof PsiMethod ? (PsiMethod) psiElement : null;
  }

  @Override
  public PsiExpression getQualifier() {
    return null;
  }

  @NotNull
  @Override
  public PsiExpression[] getArrayDimensions() {
    return PsiExpression.EMPTY_ARRAY;
  }

  @Override
  public PsiJavaCodeReferenceElement getClassReference() {
    return null;
  }

  @Override
  public PsiModifierList getModifierList() {
    return findChildByClass(PsiModifierList.class);
  }

  @Override
  public boolean hasModifierProperty(@PsiModifier.ModifierConstant @NonNls @NotNull String name) {
    PsiModifierListOwner child = findChildByClass(PsiModifierListOwner.class);
    if (child != null) {
      return child.hasModifierProperty(name);
    }
    return false;
  }


  @Nullable
  public GosuExpressionListImpl getGosuArgumentList() {
    return (GosuExpressionListImpl) findChildByType(GosuElementTypes.ELEM_TYPE_ArgumentListClause);
  }

  @Override
  public PsiArrayInitializerExpression getArrayInitializer() {
    return null;
//    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public PsiAnonymousClass getAnonymousClass() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public PsiJavaCodeReferenceElement getClassOrAnonymousClassReference() {
    return getClassRefImpl(getType());
  }

  @Nullable
  @Override
  public PsiType getOwner(@NotNull PsiAnnotation annotation) {
    return null;
  }

  @Nullable
  private PsiJavaCodeReferenceElement getClassRefImpl(@Nullable PsiType psiType) {
    if (psiType == null) {
      return null;
    } else if (psiType instanceof PsiClassReferenceType) {
      return ((PsiClassReferenceType) psiType).getReference();
    } else if (psiType instanceof PsiImmediateClassType) {
      PsiClass psiClass = ((PsiImmediateClassType) psiType).resolve();
      return new LightClassReferenceExpression(psiClass.getManager(), "", psiClass);
    } else if (psiType instanceof PsiArrayType) {
      return getClassRefImpl(((PsiArrayType) psiType).getComponentType());
    } else if (psiType instanceof PsiPrimitiveType) {
      return null;
    } else {
      throw new RuntimeException("Unknown psi type " + psiType.getClass().getName());
    }
  }

  @NotNull
  @Override
  public PsiReferenceParameterList getTypeArgumentList() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public PsiExpressionList getArgumentList() {
    return getGosuArgumentList();
  }

  @Override
  public PsiMethod resolveMethod() {
    return resolveConstructor();
  }

  @NotNull
  @Override
  public JavaResolveResult resolveMethodGenerics() {
    throw new UnsupportedOperationException("Not implemented yet");
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl) ) {
      ((JavaElementVisitor)visitor).visitCallExpression(this);
    } else if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitNewExpression(this);
    } else {
      visitor.visitElement( this );
    }
  }
}
