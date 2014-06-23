/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.List;

public abstract class PropertyInfoBase implements IPropertyInfo
{
  private ITypeInfo _container;

  protected PropertyInfoBase( ITypeInfo container )
  {
    _container = container;
  }

  public boolean isScriptable()
  {
    return true;
  }

  public boolean isDeprecated()
  {
    return false;
  }

  public String getDeprecatedReason() {
    return null;
  }

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    return true;
  }

  public boolean isHidden()
  {
    return false;
  }

  public boolean isStatic()
  {
    return false;
  }

  public boolean isPrivate()
  {
    return false;
  }

  public boolean isInternal()
  {
    return false;
  }

  public boolean isProtected()
  {
    return false;
  }

  public boolean isPublic()
  {
    return true;
  }

  public boolean isAbstract()
  {
    return false;
  }

  public boolean isFinal()
  {
    return false;
  }

  public boolean isWritable() {
    return isWritable(null);
  }

  public IPresentationInfo getPresentationInfo()
  {
    return IPresentationInfo.Default.GET;
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    return getDeclaredAnnotations();
  }

  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return ANNOTATION_HELPER.getAnnotationsOfType( type, getAnnotations() );
  }

  public boolean hasAnnotation( IType type )
  {
    return ANNOTATION_HELPER.hasAnnotation( type, getAnnotations() );
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return ANNOTATION_HELPER.getAnnotation( type, getAnnotations(), getDisplayName() );
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return ANNOTATION_HELPER.hasAnnotation( type, getDeclaredAnnotations() );
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public IType getOwnersType()
  {
    return _container.getOwnersType();
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getDescription()
  {
    return null;
  }

}