/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.psi.*;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.plugin.ij.lang.psi.api.AbstractFeatureResolver;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.custom.CustomGosuClass;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import gw.plugin.ij.lang.psi.impl.GosuResolveResultImpl;
import gw.plugin.ij.util.JavaPsiFacadeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class CatchAllFeatureResolver extends AbstractFeatureResolver {

  @Nullable
  public IGosuResolveResult resolveMethodOrConstructor(@NotNull IHasParameterInfos info, @NotNull PsiElement context) {
    IType type = info.getOwnersType();
    if (info instanceof IMethodInfo &&
        !(type instanceof IGosuClass || type instanceof IJavaType || type instanceof IBlockType || type instanceof IErrorType)) {
      CustomGosuClass psiClass = CustomPsiClassCache.instance().getPsiClass(type);
      if (psiClass != null) {
        PsiElement psiMethod = psiClass.getMethod((IMethodInfo) info, null);
        if (psiMethod != null) {
          return new GosuResolveResultImpl(psiMethod, true, info);
        }
      }
    }
    return null;
  }

}
