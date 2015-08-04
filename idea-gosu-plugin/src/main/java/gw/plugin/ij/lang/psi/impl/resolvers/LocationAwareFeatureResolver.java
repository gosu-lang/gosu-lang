/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.psi.PsiElement;
import gw.lang.reflect.IHasParameterInfos;
import gw.lang.reflect.IPropertyInfo;
import gw.plugin.ij.lang.psi.api.AbstractFeatureResolver;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.impl.GosuResolveResultImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LocationAwareFeatureResolver extends AbstractFeatureResolver {
  @Nullable
  @Override
  public PsiElement resolve(@NotNull IPropertyInfo propertyInfo, @NotNull PsiElement context) {
    if ( propertyInfo.getLocationInfo().hasLocation() ) {
      return PsiFeatureResolver.resolveFeatureAtLocation(context, propertyInfo.getLocationInfo());
    }
    return null;
  }

  @Override
  public IGosuResolveResult resolveMethodOrConstructor(@NotNull IHasParameterInfos info, @NotNull PsiElement context) {
    if ( info.getLocationInfo().hasLocation()) {
      PsiElement element = PsiFeatureResolver.resolveFeatureAtLocation(context, info.getLocationInfo());
      return new GosuResolveResultImpl(element, true, info);
    }
    return null;
  }
}
