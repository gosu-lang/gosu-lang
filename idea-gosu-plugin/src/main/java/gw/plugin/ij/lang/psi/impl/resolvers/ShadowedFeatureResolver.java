/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.psi.PsiElement;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IShadowingType;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.psi.api.AbstractFeatureResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ShadowedFeatureResolver extends AbstractFeatureResolver {

  @Nullable
  @Override
  public PsiElement resolve(@NotNull IPropertyInfo propertyInfo, @NotNull PsiElement context) {
    IType ownersType = propertyInfo.getOwnersType();

    if (ownersType instanceof IShadowingType) {
      for (IType shadowedType : ((IShadowingType) ownersType).getShadowedTypes()) {
        PsiElement psiElement = GosuFeatureResolver.resolveProperty(shadowedType, propertyInfo.getName(), context);
        if (psiElement != null) {
          return psiElement;
        }
      }
    }

    return null;
  }

}
