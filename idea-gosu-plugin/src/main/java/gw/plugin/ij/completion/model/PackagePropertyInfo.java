/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.model;

import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IPresentationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.TypeName;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 */
public class PackagePropertyInfo extends BaseFeatureInfo implements IPropertyInfo {
  @NotNull
  private final PackageType _packageType;
  private IPropertyAccessor _accessor;


  public PackagePropertyInfo(IFeatureInfo container, TypeName namespace) {
    super(container);
    _packageType = new PackageType(TypeSystem.getNamespace(container.getOwnersType().getName() + "." + namespace.name, namespace.getModule()));
  }

  public boolean isStatic() {
    return true;
  }

  public String getName() {
    return _packageType.getRelativeName();
  }

  public boolean isReadable() {
    return true;
  }

  public boolean isWritable(IType whosAskin) {
    return false;
  }

  public boolean isWritable() {
    return isWritable(null);
  }

  @NotNull
  public IPropertyAccessor getAccessor() {
    return _accessor == null ? _accessor = new PackagePropertyAccessor() : _accessor;
  }

  public IPresentationInfo getPresentationInfo() {
    return IPresentationInfo.Default.GET;
  }

  @NotNull
  public IType getFeatureType() {
    return _packageType;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  private class PackagePropertyAccessor implements IPropertyAccessor {
    @NotNull
    public Object getValue(Object ctx) {
      return _packageType;
    }

    public void setValue(Object ctx, Object value) {
      throw new UnsupportedOperationException();
    }
  }
}
