/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.api;

import com.intellij.psi.PsiElement;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.IHasParameterInfos;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IFeatureResolver {
  @Nullable
  PsiElement resolve(IPropertyInfo propertyInfo, PsiElement context);
  @Nullable
  PsiElement resolve(IReducedSymbol symbol, IGosuClass gsClass, GosuReferenceExpressionImpl context);
  @Nullable
  IGosuResolveResult resolveMethodOrConstructor(@NotNull IHasParameterInfos info, @NotNull PsiElement ctx);
  @Nullable
  PsiElement resolveMethodOrConstructor(@NotNull IFunctionSymbol symbol, @NotNull PsiElement ctx);
}

