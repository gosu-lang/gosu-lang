/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameter;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IDynamicSymbol;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuProgram;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierExpressionImpl;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiTypeResolver;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SymbolCompletionProposal extends GosuCompletionProposal {
  private final ISymbol _symbol;
  private PsiElement context;

  public SymbolCompletionProposal(ISymbol symbol, int weight) {
    _symbol = symbol;
    setWeight(weight);
  }

  @Override
  public String toString() {
    return _symbol.getDisplayName();
  }

  public ISymbol getSymbol() {
    return _symbol;
  }

  @Nullable
  @Override
  public PsiElement resolve(@NotNull PsiElement context) {
    this.context = context;
    PsiFile psiFile = context.getContainingFile().getOriginalFile();
    IClassFileStatement classFileStatement = ((AbstractGosuClassFileImpl) psiFile).getParseData().getClassFileStatement();
    IGosuClass gsClass = classFileStatement.getGosuClass();
    if ((Keyword.KW_this.equals(_symbol.getName()) || Keyword.KW_super.equals(_symbol.getName())) &&
        // 'this' must be an external symbol when in a program e.g., studio debugger expression
        (!(gsClass instanceof IGosuProgram) || gsClass.isAnonymous())) {
      if (gsClass instanceof IBlockClass) {
        while (gsClass instanceof IBlockClass) {
          gsClass = (IGosuClass) gsClass.getEnclosingType();
        }
      }
      return PsiTypeResolver.resolveType(gsClass, this.context);
    } else if (Keyword.KW_outer.equals(_symbol.getName())) {
      // 'outer'
      return resolveOuter(gsClass);
    } else {
      return resolveSymbol(_symbol, gsClass);
    }
  }

  @Nullable
  @Override
  public IFeatureInfo getFeatureInfo() {
    if (_symbol instanceof IDynamicFunctionSymbol) {
      return ((IDynamicFunctionSymbol) _symbol).getMethodOrConstructorInfo();
    }

    if (_symbol instanceof IDynamicPropertySymbol) {
      return ((IDynamicPropertySymbol) _symbol).getPropertyInfo();
    }
    return null;
  }

  @Override
  public String getGenericName() {
    return _symbol.getName();
  }

  @Nullable
  protected PsiElement resolveOuter(IGosuClass gsClass) {
    while (gsClass instanceof IBlockClass) {
      gsClass = (IGosuClass) gsClass.getEnclosingType();
    }
    if (gsClass == null || gsClass.getEnclosingType() == null) {
      return null;
    }
    return PsiTypeResolver.resolveType(gsClass.getEnclosingType(), context);
  }

  //TODO-dp this duplicates logic in GosuIdentifierExpressionImpl
  @Nullable
  protected PsiElement resolveSymbol(@NotNull IReducedSymbol symbol, @NotNull IGosuClass gsClass) {
    Class symClass = symbol.getClass();
    if (gsClass.getExternalSymbol(getReferenceName()) != null) {
      // Currently we do not attempt to link to decl source of external symbols

      return null;
    }
    if (IDynamicFunctionSymbol.class.isAssignableFrom(symClass)) {
      if (!symbol.isStatic()) {
        if (isMemberOnEnclosingType(symbol, gsClass)) {
          // Instance field from 'outer'
          return resolveFunction(getReferenceName(), getSymbolType(symbol).getEnclosingType());
        } else {
          // Instance field from 'this'
          IType symbolType = getSymbolType(symbol);
          if(symbolType != null && getReferenceName() != null) {
            return resolveFunction(getReferenceName(), symbolType);
          }
        }
      } else {
        // Static field
        return resolveFunction(getReferenceName(), getSymbolType(symbol));
      }
    } else if (IDynamicSymbol.class.isAssignableFrom(symClass)) {
      // Instance or Static field

      if (!symbol.isStatic()) {
        if (isMemberOnEnclosingType(symbol, gsClass)) {
          // Instance field from 'outer'
          return resolveField(getReferenceName(), getSymbolType(symbol).getEnclosingType());
        } else {
          // Instance field from 'this'
          return resolveField(getReferenceName(), getSymbolType(symbol));
        }
      } else {
        // Static field
        return resolveField(getReferenceName(), getSymbolType(symbol));
      }
    } else if (ICapturedSymbol.class.isAssignableFrom(symClass)) {
      return resolveCapture(getReferenceName(), gsClass);
    } else if (symbol.getIndex() >= 0) {
      // Local var

      if (symbol.isValueBoxed()) {
        // Local var is captured in an anonymous inner class.
        // Symbol's value maintained as a one elem array of symbol's type.
        return resolveCapture(getReferenceName(), gsClass);
      } else {
        // Simple local var
        return resolveLocalVar(getReferenceName());
      }
    } else if (IDynamicPropertySymbol.class.isAssignableFrom(symClass)) {
      throw new UnsupportedOperationException("Men at work");
    }
    return null;
  }

  private IType getSymbolType(@NotNull IReducedSymbol symbol) {
    return GosuIdentifierExpressionImpl.maybeUnwrapProxy(symbol.getGosuClass());
  }

  protected boolean isMemberOnEnclosingType(@NotNull IReducedSymbol symbol, @NotNull IGosuClass gsClass) {
    if (!gsClass.isStatic() && gsClass.getEnclosingType() != null) {
      return false;
    }

    // If the symbol is on this class, or any ancestors, it's not enclosed
    //noinspection SuspiciousMethodCalls
    IType symbolClass = GosuIdentifierExpressionImpl.maybeUnwrapProxy(getSymbolType(symbol));
    if (gsClass.getAllTypesInHierarchy().contains(symbolClass)) {
      return false;
    }

    ICompilableType enclosingClass = gsClass.getEnclosingType();
    while (enclosingClass != null) {
      //noinspection SuspiciousMethodCalls
      if (enclosingClass.getAllTypesInHierarchy().contains(symbolClass)) {
        return true;
      }
      enclosingClass = enclosingClass.getEnclosingType();
    }

    return false;
  }

  @Nullable
  private PsiElement resolveCapture(String strName, @NotNull IGosuClass gsClass) {
    return resolveField(strName, gsClass);
  }

  @Nullable
  protected PsiElement resolveField(String strField, @NotNull IType gsClass) {
    return resolveField(strField, PsiTypeResolver.resolveType(gsClass, context));
  }

  @Nullable
  protected PsiElement resolveField(String strField, @Nullable PsiElement psiClass) {
    if (psiClass instanceof PsiClass) {
      final PsiField field = ((PsiClass)psiClass).findFieldByName(strField, false);
      return field != null ? field : resolveProperty(strField, ((PsiClass)psiClass));
    }
    return null;
  }

  @Nullable
  protected PsiElement resolveFunction(String strField, @NotNull IType gsClass) {
    return resolveFunction(strField, PsiTypeResolver.resolveType(gsClass, context));
  }

  @Nullable
  protected PsiElement resolveFunction(String strField, @NotNull PsiElement psiClass) {
    if (psiClass instanceof PsiClass) {
      for (PsiMethod method : ((PsiClass)psiClass).getAllMethods()) {
        if (makeMethodName(method).equals(strField)) {
          return method;
        }
      }
    }
    return null;
  }

  private String makeMethodName(@NotNull PsiMethod method) {
    StringBuilder sb = new StringBuilder(method.getName()).append("(");
    PsiParameter[] parameters = method.getParameterList().getParameters();
    for (int i = 0; i < parameters.length; i++) {
      if (i != 0) {
        sb.append(", ");
      }
      sb.append(parameters[i].getType().getCanonicalText());
    }
    return sb.append(")").toString();
  }

  @Nullable
  protected PsiElement resolveProperty(String strProperty, String strFqn) {
    return resolveProperty(strProperty, PsiTypeResolver.resolveType(strFqn, context));
  }

  @Nullable
  protected PsiElement resolveProperty(String strProperty, @NotNull PsiClass psiClass) {
    //## todo: check for this ref expr being the lhs of an assignment and look for setter instead

    for (PsiMethod method : psiClass.getMethods()) {
      if (method.getName().equals(strProperty) && method.getParameterList().getParametersCount() == 0) {
        return method;
      }
    }

    for (PsiField field : psiClass.getFields()) {
      if (field instanceof IGosuField) {
        IGosuField gsField = (IGosuField) field;
        String strPropertyName = gsField.getPropertyName();
        if (strPropertyName != null && strPropertyName.equals(strProperty)) {
          return ((IGosuField) field).getPropertyElement();
        }
      }
    }

    String strGetProperty = "get" + strProperty;
    String strIsProperty = "is" + strProperty;
    for (PsiMethod method : psiClass.getMethods()) {
      if ((method.getName().equals(strGetProperty) || method.getName().equals(strIsProperty)) &&
          method.getParameterList().getParametersCount() == 0) {
        return method;
      }
    }

    return null;
  }

  @Nullable
  private PsiElement resolveLocalVar(String strName) {
    return resolveLocal(strName);
  }

  @Nullable
  protected PsiElement resolveLocal(@Nullable String strLocalVar) {
    if (strLocalVar != null) {
      if (context != null) {
        return JavaPsiFacadeUtil.getResolveHelper(context.getProject()).resolveReferencedVariable(strLocalVar, context);
      }
    }
    return null;
  }

  private String getReferenceName() {
    return _symbol.getName();
  }
}
