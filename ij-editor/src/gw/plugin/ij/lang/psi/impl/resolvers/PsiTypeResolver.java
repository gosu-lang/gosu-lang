/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.util.NotNullLazyValue;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.PsiTypeParameterListOwner;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.lang.psi.api.ITypeResolver;
import gw.plugin.ij.util.ExecutionUtil;
import gw.plugin.ij.util.SafeCallable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiTypeResolver {
  private static final NotNullLazyValue<ITypeResolver[]> resolvers = new NotNullLazyValue<ITypeResolver[]>() {
    @NotNull
    @Override
    protected ITypeResolver[] compute() {
      final TypeResolverExtensionBean[] extensions = Extensions.getExtensions(TypeResolverExtensionBean.EP_NAME);
      final ITypeResolver[] resolvers = new ITypeResolver[extensions.length];
      for (int i = 0; i < resolvers.length; i++) {
        resolvers[i] = extensions[i].getHandler();
      }
      return resolvers;
    }
  };


  @Nullable
  public static PsiElement resolveType(@NotNull final IType type, @NotNull final PsiElement ctx) {
    if (TypeSystem.isDeleted(type)) {
      return null;
    }

    return ExecutionUtil.execute(new SafeCallable<PsiElement>(ctx) {
      public PsiElement execute() throws Exception {
        for (ITypeResolver resolver : resolvers.getValue()) {
          final PsiElement element = resolver.resolveType(type, ctx);
          if (element != null) {
            return element;
          }
        }
        return null;
      }
    });
  }

  @Nullable
  public static PsiClass resolveType(final String strFullName, @NotNull final PsiElement ctx) {
    return ExecutionUtil.execute(new SafeCallable<PsiClass>(ctx) {
      public PsiClass execute() throws Exception {
        for (ITypeResolver resolver : resolvers.getValue()) {
          PsiClass element = resolver.resolveType(strFullName, ctx);
          if (element != null) {
            return element;
          }
        }
        return null;
      }
    });
  }

  @Nullable
  public static PsiElement resolveTypeVariable(@NotNull final ITypeVariableType tv, @NotNull final PsiElement ctx) {
    return ExecutionUtil.execute(new SafeCallable<PsiElement>(ctx) {
      public PsiElement execute() throws Exception {
        IType encType = tv.getEnclosingType();
        PsiTypeParameterListOwner psiTypeParamOwner = null;
        if (encType instanceof IFunctionType) {
          IMethodInfo methodInfo = ((IFunctionType) encType).getMethodInfo();
          if (methodInfo != null) {
            psiTypeParamOwner = (PsiTypeParameterListOwner) PsiFeatureResolver.resolveMethodOrConstructor(methodInfo, ctx);
          }
        } else {
          psiTypeParamOwner = resolveType(getTypeName(encType), ctx);
        }

        if (psiTypeParamOwner != null) {
          return findTypeVariable(tv, psiTypeParamOwner);
        } else {
          return null;
        }
      }
    });
  }

  @Nullable
  public static PsiElement findTypeVariable(@NotNull ITypeVariableType tv, @NotNull PsiTypeParameterListOwner psiTypeParamOwner) {
    for (PsiTypeParameter param : psiTypeParamOwner.getTypeParameters()) {
      if (param.getName().equals(tv.getRelativeName())) {
        return param;
      }
    }
    return null;
  }

  @Nullable
  public static PsiElement getProxiedPsiClass(@NotNull final IType proxyType, @NotNull final PsiElement ctx) {
    return ExecutionUtil.execute(new SafeCallable<PsiElement>(ctx) {
      public PsiElement execute() throws Exception {
        String fqProxiedClassName = IGosuClass.ProxyUtil.getNameSansProxy(proxyType.getName());
        IType proxiedType = TypeSystem.getByFullNameIfValid(fqProxiedClassName);
        return resolveType(proxiedType, ctx);
      }
    });
  }

  public static String getTypeName(@NotNull IType type) {
    while (type.isParameterizedType()) {
      type = type.getGenericType();
    }
    return type.getName();
  }
}
