/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;
import org.jetbrains.annotations.NotNull;

public class PropertyInfoNode extends BeanInfoNode {
  @NotNull
  private final IPropertyInfo propertyInfo;

  public PropertyInfoNode(@NotNull IPropertyInfo propertyInfo) {
    super(propertyInfo.getFeatureType());

    this.propertyInfo = propertyInfo;
  }

  @Override
  public String getDisplayName() {
    if (propertyInfo instanceof PackagePropertyInfo) {
      return propertyInfo.getName();
    } else if (propertyInfo instanceof TypePropertyInfo) {
      return propertyInfo.getName() + " - " + getOwnersTypeName();
    } else {
      return propertyInfo.getName()
          + (getType() == null ? "" : (" : " + getTypeName(getType())))
          + (propertyInfo.getOwnersType() == null ? "" : " - " + getOwnersTypeName());
    }
  }

  private String getOwnersTypeName() {
    IType ownersType = propertyInfo.getOwnersType();
    if (ownersType instanceof IMetaType) {
      ownersType = ((IMetaType) ownersType).getType();
    }

    if (ownersType instanceof IGosuClass && ((IGosuClass) ownersType).isAnonymous()) {
      IType supertype = ownersType.getSupertype();
      if (supertype == null) {
        supertype = ownersType.getInterfaces()[0];
      }
      return ownersType.getEnclosingType().getName() + ".new " + supertype.getRelativeName() + "(){...}";
    } else {
      return ownersType.getDisplayName();
    }
  }

  @Override
  public String getName() {
    return propertyInfo.getName();
  }

  @NotNull
  @Override
  public IFeatureInfo getFeatureInfo() {
    return propertyInfo;
  }

  @Override
  protected int getTypePriority() {
    if (propertyInfo instanceof TypePropertyInfo) {
      return 5;
    }
    return propertyInfo.isStatic() ? 3 : 0;
  }
}
