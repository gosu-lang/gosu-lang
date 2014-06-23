/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IDynamicSymbol;
import gw.lang.parser.IInitializerSymbol;
import gw.lang.parser.IReducedSymbol;
import gw.lang.parser.ITypedSymbol;
import gw.lang.parser.Keyword;
import gw.lang.parser.SymbolType;
import gw.lang.parser.expressions.IMethodCallExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.ITemplateType;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.AbstractFeatureResolver;
import gw.plugin.ij.lang.psi.api.statements.IGosuField;
import gw.plugin.ij.lang.psi.api.statements.params.IGosuParameter;
import gw.plugin.ij.lang.psi.impl.GosuBaseElementImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuDirectiveExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuNewExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuVariableImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuClassDefinitionImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.members.GosuMethodImpl;
import gw.plugin.ij.util.InjectedElementEditor;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import gw.plugin.ij.util.TypeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

import static com.google.common.collect.Iterables.filter;

public class GosuFeatureResolver extends AbstractFeatureResolver {

  @Nullable
  @Override
  public PsiElement resolve(@NotNull IPropertyInfo propertyInfo, @NotNull PsiElement context) {
    IType ownersType = propertyInfo.getOwnersType();
    PsiElement psiElement = resolveProperty(ownersType, propertyInfo.getName(), context);
    if (psiElement != null) {
      return psiElement;
    }
    // property could be in a super interface and implemented via a delegate
    for (IType anInterface : ownersType.getInterfaces()) {
      psiElement = resolveProperty(anInterface, propertyInfo.getName(), context);
      if (psiElement != null) {
        return psiElement;
      }
    }
    return null;
  }

  public static PsiElement resolveProperty(IType ownersType, String propertyName, PsiElement context) {
    ownersType = TypeUtil.getConcreteType(ownersType);
    PsiElement resolved = null;
    if (IGosuClass.ProxyUtil.isProxyClass(ownersType.getName())) {
      PsiElement proxiedPsiClass = PsiTypeResolver.getProxiedPsiClass(ownersType, context);
      if (proxiedPsiClass instanceof PsiClass) {
        return findInheritedPropertyAcrossProxy((PsiClass) proxiedPsiClass,propertyName, context);
      }
    } else if ("outer".equals(propertyName)) {
      resolved = PsiTypeResolver.resolveType(TypeUtil.getTrueEnclosingType(ownersType), context);
    } else {
      PsiElement psiClass = PsiTypeResolver.resolveType(ownersType, context);
      if (psiClass instanceof PsiClass) {
        resolved = resolveProperty(propertyName, (PsiClass) psiClass, context);
      }
    }
    return resolved;
  }

  @Nullable
  private static PsiElement findInheritedPropertyAcrossProxy(@Nullable PsiClass targetClass, @NotNull String targetPropertyName, @NotNull PsiElement ctx) {
    // TODO: use GosuProperties class somehow
    if (targetClass == null) {
      return null;
    }
    boolean bSetter = ctx instanceof GosuReferenceExpressionImpl && ((GosuReferenceExpressionImpl) ctx).isAssigned();
    PsiMethod candidate = null;
    if (bSetter) {
      String propName = targetPropertyName.startsWith("set") ? targetPropertyName.substring(3) : targetPropertyName;
      String setPropertyName = "set" + propName;
      for (PsiMethod method : targetClass.getMethods()) {
        if (method.getName().equals(setPropertyName) && method.getParameterList().getParametersCount() == 1) {
          return method;
        }
        if (method.getName().equalsIgnoreCase(setPropertyName) && method.getParameterList().getParametersCount() == 1) {
          candidate = method;
        }
      }
    } else {
      String propName = targetPropertyName.startsWith("get") ? targetPropertyName.substring(3) :
          targetPropertyName.startsWith("is") ? targetPropertyName.substring(2) : targetPropertyName;
      String getPropertyName = "get" + propName;
      String isPropertyName = "is" + propName;
      for (PsiMethod method : targetClass.getMethods()) {
        String methodName = method.getName();
        if ((getPropertyName.equals(methodName) || isPropertyName.equals(methodName)) && method.getParameterList().getParametersCount() == 0) {
          return method;
        }
        if ((getPropertyName.equalsIgnoreCase(methodName) || isPropertyName.equalsIgnoreCase(methodName)) && method.getParameterList().getParametersCount() == 0) {
          candidate = method;
        }
      }
    }
    if (candidate != null) {
      return candidate;
    }
    return findInheritedPropertyAcrossProxy(targetClass.getSuperClass(), targetPropertyName, ctx);
  }

  @Nullable
  public static PsiElement resolveProperty(String strProperty, @Nullable PsiClass psiClass, @NotNull PsiElement ctx) {
    // TODO: use GosuProperties class somehow
    boolean bSetter = (ctx instanceof GosuReferenceExpressionImpl) && ((GosuReferenceExpressionImpl) ctx).isAssigned();

    if (psiClass == null) {
      return null;
    }

    if (bSetter) {
      String strSetProperty = "set" + strProperty;
      for (PsiMethod method : psiClass.getMethods()) {
        if (method.getName().equals(strSetProperty) &&
            method.getParameterList().getParametersCount() == 1) {
          return method;
        }
      }
    } else {
      String strGetProperty = "get" + strProperty;
      String strIsProperty = "is" + strProperty;
      for (PsiMethod method : psiClass.getMethods()) {
        if ((method.getName().equals(strGetProperty) || method.getName().equals(strIsProperty)) &&
            method.getParameterList().getParametersCount() == 0) {
          return method;
        }
      }
    }

    for (PsiMethod method : psiClass.getMethods()) {
      if (method instanceof GosuMethodImpl &&
          ((GosuMethodImpl)method).isForProperty() &&
          method.getName().equals(strProperty) &&
          method.getParameterList().getParametersCount() == (bSetter ? 1 : 0)) {
        return method;
      }
    }

    for (PsiField field : psiClass.getFields()) {
      // Try field name first
      String strPropertyName = field.getName();
      if (strPropertyName != null && strPropertyName.equals(strProperty)) {
        return field;
      }

      if (field instanceof IGosuField) {
        // Then try aliased property name
        strPropertyName = ((IGosuField) field).getPropertyName();
        if (strPropertyName != null && strPropertyName.equals(strProperty)) {
          return ((IGosuField) field).getPropertyElement();
        }
      }
    }



    return null;
  }

  @Nullable
  private static PsiField findInheritedFieldAcrossProxy(@Nullable PsiClass targetClass, String targetFieldName) {
    PsiField result = null;
    if (targetClass != null) {
      result = targetClass.findFieldByName(targetFieldName, false);
      if (result == null) {
        result = findInheritedFieldAcrossProxy(targetClass.getSuperClass(), targetFieldName);
      }
    }
    return result;
  }

  @Nullable
  @Override
  public PsiElement resolve(@NotNull IReducedSymbol symbol, @Nullable IGosuClass gsClass,final @NotNull GosuReferenceExpressionImpl expression) {
    if (symbol.getType() instanceof INamespaceType) {
      String packageName = symbol.getType().getName();
      PsiPackage aPackage = JavaPsiFacade.getInstance(expression.getProject()).findPackage(packageName);
      return  (aPackage == null || !aPackage.isValid()) ? null : aPackage;
    }
    if (gsClass == null) {
      return null;
    }
    if (symbol.isValueBoxed() && gsClass.isAnonymous()) {
      gsClass = (IGosuClass) gsClass.getEnclosingType();
    }

    Class symClass = symbol.getClass();
    IType symbolType = symbol.getType();

    if (symbolType instanceof IMetaType) {
      IType trueSymbolType = ((IMetaType) symbolType).getType();
      if (trueSymbolType instanceof ITypeVariableType) {
        return PsiTypeResolver.resolveTypeVariable((ITypeVariableType) trueSymbolType, expression);
      }
    }

    if (symbol instanceof ITypedSymbol) {
      SymbolType symbolKind = ((ITypedSymbol) symbol).getSymbolType();
      switch (symbolKind) {
        case CATCH_VARIABLE:
          return resolveCatchLocalVar(symbol.getName(), expression);
        case OBJECT_INITIALIZER:
          return resolveProperty(symbol.getName(), ((IInitializerSymbol)symbol).getDeclaringTypeOfProperty(), expression);
        case NAMED_PARAMETER:
          return resolveNamedParameter(symbol.getName(), expression);
        case PARAMETER_DECLARATION:
        case FOREACH_VARIABLE:
          return resolveLocalVar(symbol.getName(), expression);
        default:
          return null;
//          throw new RuntimeException("Unsupported typed symbol " + symbolKind);
      }
    }

    if (symbolType instanceof IMetaType) {
      final IType trueSymbolType = ((IMetaType) symbolType).getType();
      if (trueSymbolType instanceof ITypeVariableType) {
        return PsiTypeResolver.resolveTypeVariable((ITypeVariableType) trueSymbolType, expression);
      }
    }

    String referenceName = expression.getReferenceName();
    if (IDynamicPropertySymbol.class.isAssignableFrom(symClass)) {
      if (Keyword.KW_outer.equals(symbol.getName())) {
        // 'outer'
        return resolveOuter(gsClass, expression);
      }
      // Property reference
      if (!symbol.isStatic()) {
        IGosuClass symbolGosuClass = gsClass;
        if (isMemberOnEnclosingType(gsClass, symbolGosuClass)) {
          // Instance field from 'outer'
          return resolveProperty(referenceName, symbolGosuClass.getEnclosingType(), expression);
        } else {
          // Instance field from 'this'
//          return resolveProperty( getReferenceName(), symbolGosuClass );
          return resolveProperty(symbol.getName(), symbolGosuClass, expression);
        }
      } else {
        // Static field
        return resolveProperty(referenceName, gsClass, expression);
      }
    }

    if (IDynamicSymbol.class.isAssignableFrom(symClass)) {
      // Instance or Static field
      IGosuClass symbolGosuClass = gsClass;
      if (!symbol.isStatic()) {
        if (isMemberOnEnclosingType(gsClass, symbolGosuClass)) {
          // Instance field from 'outer'
          return resolveField(referenceName, symbolGosuClass.getEnclosingType(), expression);
        } else {
          // Instance field from 'this'
          return resolveField(referenceName, symbolGosuClass, expression);
        }
      } else {
        // Static field
        return resolveField(referenceName, gsClass, expression);
      }
    }

    PsiElement ret = null;
    if (ICapturedSymbol.class.isAssignableFrom(symClass)) {
      ret = resolveField(referenceName, gsClass, expression);
    }

    if (ret == null && symbol.getIndex() >= 0) {
      // Local var
      if (symbol.isValueBoxed()) {
        // Local var is captured in an anonymous inner class.
        ret = resolveField(referenceName, gsClass, expression);
      }
      if (ret == null) {
        // Simple local var
        ret = resolveLocalVar(referenceName, expression);

        // Maybe a template param directive var
        if (couldBeTemplateParamDirective(symbol, gsClass, ret)) {
          return resolveTemplateParameter(symbol.getName(), expression);
        }
      }
      return ret;
    }

    return null;
  }

  public static PsiElement resolveProperty(String strProperty, @NotNull IType type, PsiElement ctx) {

    ITypeInfo typeInfo = type.getTypeInfo();
    IPropertyInfo pi;
    if( typeInfo instanceof IRelativeTypeInfo ) {
      pi = ((IRelativeTypeInfo) typeInfo).getProperty(type, strProperty);
    }
    else {
      pi = typeInfo.getProperty(strProperty);
    }
    return pi != null ? PsiFeatureResolver.resolveProperty(pi, ctx) : null;
  }

  public static PsiElement resolveField(String strField, @NotNull IType type, PsiElement ctx) {
    final ITypeInfo typeInfo = type.getTypeInfo();
    final IPropertyInfo property;
    if (typeInfo instanceof IRelativeTypeInfo) {
      property = ((IRelativeTypeInfo) typeInfo).getProperty(type, strField);
    } else {
      property = typeInfo.getProperty(strField);
    }
    return property == null ? null : PsiFeatureResolver.resolveProperty(property, ctx);
  }

  @Nullable
  private PsiElement resolveCatchLocalVar(String name, @NotNull PsiElement context) {
    if (context.getParent() instanceof GosuBaseElementImpl) {
      GosuBaseElementImpl element = (GosuBaseElementImpl) context.getParent();
      while (element != null) {
        if (element.getGosuElementType().equals(GosuElementTypes.ELEM_TYPE_CatchClause)) {
          GosuVariableImpl var = (GosuVariableImpl) element.getNode().findChildByType(GosuElementTypes.ELEM_TYPE_LocalVarDeclaration).getPsi();
          if (var.getName().equals(name)) {
            final PsiElement maybeInjectedElement = InjectedElementEditor.getOriginalElement(var);
            return  maybeInjectedElement == null ? var : PsiTreeUtil.getParentOfType(maybeInjectedElement, var.getClass(),false);
          }
        }
        if( element.getParent() instanceof GosuBaseElementImpl ) {
          element = (GosuBaseElementImpl) element.getParent();
        } else {
          element = null;
        }
      }
    }
    return null;
  }

  @Nullable
  private PsiElement resolveNamedParameter(String paramName, @NotNull PsiElement context) {
    PsiElement parent = context.getParent();
    if (parent.getNode().getElementType() == GosuElementTypes.ELEM_TYPE_ArgumentListClause) {
      parent = parent.getParent();
    }
    IFeatureInfo info = null;
    if (parent instanceof GosuMethodCallExpressionImpl) {
      IMethodCallExpression parsedElement = ((GosuMethodCallExpressionImpl) parent).getParsedElement();
      IFunctionType functionType = parsedElement.getFunctionType();
      if (functionType != null) {
        info = functionType.getMethodOrConstructorInfo();
      }
    } else if (parent instanceof GosuNewExpressionImpl) {
      INewExpression parsedElement = ((GosuNewExpressionImpl) parent).getParsedElement();
      info = parsedElement.getConstructor();
    }
    if (info != null) {
      PsiElement element = PsiFeatureResolver.resolveMethodOrConstructor((IHasParameterInfos) info, context);
      if (element instanceof PsiMethod) {
        PsiParameter[] parameters = ((PsiMethod) element).getParameterList().getParameters();
        for (PsiParameter parameter : parameters) {
          if (parameter.getName().equals(paramName)) {
            return parameter;
          }
        }
      } else if (element instanceof PsiClass && ((PsiClass) element).isAnnotationType()) {
        PsiMethod[] methodsByName = ((PsiClass) element).findMethodsByName(paramName, false);
        return methodsByName.length != 0 ? methodsByName[0] : null;
      }
    }
    return null;
  }

  private boolean couldBeTemplateParamDirective(IReducedSymbol symbol, IGosuClass gsClass, @Nullable PsiElement ret) {
    return ret == null && symbol instanceof ITypedSymbol && gsClass instanceof ITemplateType;
  }

  @Nullable
  private PsiElement resolveLocalVar(@Nullable String strName, @NotNull GosuReferenceExpressionImpl expr) {
    if (strName != null) {
      PsiElement context = expr.getContext();
      if (context != null) {
        final PsiVariable psiVariable = JavaPsiFacadeUtil.getResolveHelper(context.getProject()).resolveReferencedVariable(strName, context);
        final PsiElement maybeInjectedElement = psiVariable == null ? null : InjectedElementEditor.getOriginalElement(psiVariable);
        return maybeInjectedElement == null ? null : PsiTreeUtil.getParentOfType(maybeInjectedElement, psiVariable.getClass(),false);
      }
    }
    return null;
  }

  protected PsiElement resolveOuter(IGosuClass gsClass, PsiElement context) {
    IType namedEnclosingType = gsClass instanceof IBlockClass ? TypeUtil.getTrueEnclosingType(gsClass) : gsClass;
    return PsiTypeResolver.resolveType(TypeUtil.getTrueEnclosingType(namedEnclosingType), context);
  }

  protected boolean isMemberOnEnclosingType(@NotNull IGosuClass gsClass, IGosuClass symbolClass) {
    if (!gsClass.isStatic() && gsClass.getEnclosingType() != null) {
      return false;
    }

    // If the symbol is on this class, or any ancestors, it's not enclosed
    //noinspection SuspiciousMethodCalls
    //IType symbolClass = maybeUnwrapProxy(symbol.getGosuClass());
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
  private PsiElement resolveTemplateParameter(String name, PsiElement context) {
    PsiElement c = context;
    while (!(c instanceof GosuClassDefinitionImpl)) {
      c = c.getParent();
    }

    for (GosuDirectiveExpressionImpl child : filter(Arrays.asList(c.getChildren()), GosuDirectiveExpressionImpl.class)) {
      for (IGosuParameter parameter : child.getParameters()) {
        if (parameter.getName().equals(name)) {
          return parameter;
        }
      }
    }
    return null;
  }
}
