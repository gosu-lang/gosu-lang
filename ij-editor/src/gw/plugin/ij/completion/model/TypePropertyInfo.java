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
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 */
public class TypePropertyInfo extends BaseFeatureInfo implements IPropertyInfo {
  @NotNull
  private IPropertyAccessor _accessor;
  private TypeInPackageType _type;

  public TypePropertyInfo(IFeatureInfo container, TypeInPackageType type) {
    super(container);
    _type = type;
  }

  public boolean isStatic() {
    return true;
  }

  public String getName() {
    return _type.getRelativeName();
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
    return _accessor == null ? _accessor = new MetaTypePropertyAccessor() : _accessor;
  }

  public IPresentationInfo getPresentationInfo() {
    return IPresentationInfo.Default.GET;
  }

  @NotNull
  public IType getFeatureType() {
    return _type;
  }

  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  private class MetaTypePropertyAccessor implements IPropertyAccessor {
    @NotNull
    public Object getValue(Object ctx) {
      return _type;
    }

    public void setValue(Object ctx, Object value) {
      throw new UnsupportedOperationException();
    }
  }

  public String toString() {
    return getName();
  }

  public boolean isHidden() {
    //TODO-dp implement visibility rules here
//    _type._typeName.visibility
    return false;
  }

  public boolean isScriptable() {
    //TODO-dp implement visibility rules here
    return true;
  }

  @Override
  public boolean isDeprecated() {
    //TODO-dp implement visibility rules here
    return false;
  }
}
