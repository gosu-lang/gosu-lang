/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import com.google.common.collect.ComparisonChain;
import gw.lang.IModuleAware;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BeanInfoNode implements Comparable<BeanInfoNode> {
  private IType type;
  private String displayName;

  public BeanInfoNode(IType type) {
    this.type = TypeSystem.isDeleted(type) ? TypeSystem.getErrorType() : type;
  }

  public BeanInfoNode(String displayName) {
    this.displayName = displayName;
  }

  public IType getType() {
    return type;
  }

  public IModule getModule() {
    if (type instanceof IModuleAware) {
      return ((IModuleAware) type).getModule();
    } else if (type instanceof PackageType) {
      return ((PackageType) type).getNamespaceType().getModule();
    } else {
      return TypeSystem.getModuleFromType(type);
    }
  }

  public String getName() {
    return getDisplayName();
  }

  @Nullable
  public IFeatureInfo getFeatureInfo() {
    return null;
  }

  protected int getTypePriority() {
    return 0;
  }

  // DisplayName
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  // ToString
  public String toString() {
    return type.getName();
  }

  // Comparable<BeanInfoNode>
  public int compareTo(@NotNull BeanInfoNode o) {
    return ComparisonChain.start()
        .compare(getTypePriority(), o.getTypePriority())
        .compare(getDisplayName(), o.getDisplayName(), String.CASE_INSENSITIVE_ORDER)
        .result();
  }

  public static String getTypeName(@NotNull IType type) {
    return type.isArray() ? getTypeName(type.getComponentType()) + "[]" : type.getRelativeName();
  }
}
