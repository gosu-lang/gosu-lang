/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IType;
import gw.lang.reflect.PropertyInfoBase;
import gw.lang.reflect.java.JavaTypes;

import java.util.Collections;
import java.util.List;

/**
 * PropertyInfo implementation for properties within a {@link PropertiesTypeInfo}
 */
class PropertiesPropertyInfo extends PropertyInfoBase implements IPropertiesPropertyInfo {

  private final PropertyNode _propertyNode;
  private final boolean _isStatic;
  private final IPropertyAccessor _accessor = new IPropertyAccessor() {

    @Override
    public Object getValue(Object ctx) {
      return _propertyNode.isLeaf() ? _propertyNode.getValue() : _propertyNode;
    }

    @Override
    public void setValue(Object ctx, Object value) {
      throw new UnsupportedOperationException("Should never be set, property is not writeable");
    }
    
  };
  
  public PropertiesPropertyInfo(PropertiesTypeInfo typeInfo, PropertyNode propertyNode, boolean isStatic) {
    super(typeInfo);
    _propertyNode = propertyNode;
    _isStatic = isStatic;
  }

  @Override
  public IPropertyAccessor getAccessor() {
    return _accessor;
  }

  @Override
  public boolean isReadable() {
    return true;
  }

  @Override
  public boolean isWritable(IType whosAskin) {
    return false;
  }

  @Override
  public boolean isStatic() {
    return _isStatic;
  }

  @Override
  public List<IAnnotationInfo> getDeclaredAnnotations() {
    return Collections.emptyList();
  }

  @Override
  public String getName() {
    return _propertyNode.getRelativeName();
  }

  @Override
  public IType getFeatureType() {
    return _propertyNode.isLeaf() ? JavaTypes.STRING() : _propertyNode.getIntrinsicType();
  }

  @Override
  public boolean hasAnnotation(IType type) {
    return false;
  }

  public String getPropertyEntryName()
  {
    return _propertyNode.getPath();
  }

  @Override
  public int getOffset()
  {
    return ((PropertiesType)getOwnersType()).findOffsetOf( _propertyNode );
  }
}
