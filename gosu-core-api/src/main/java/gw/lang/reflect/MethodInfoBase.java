/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.Collections;
import java.util.List;

public abstract class MethodInfoBase implements IMethodInfo
{
  private ITypeInfo _container;

  protected MethodInfoBase( ITypeInfo container )
  {
    _container = container;
  }

  public String getReturnDescription()
  {
    return null;
  }

  public List<IExceptionInfo> getExceptions()
  {
    return Collections.emptyList();
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

  public boolean isDefaultImpl() {
    return false;
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

  public List<IAnnotationInfo> getAnnotations()
  {
    return getDeclaredAnnotations();
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return ANNOTATION_HELPER.getAnnotation(type, getAnnotations(), getDisplayName());
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return ANNOTATION_HELPER.hasAnnotation(type, getDeclaredAnnotations());
  }

  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return ANNOTATION_HELPER.getAnnotationsOfType(type, getAnnotations());
  }

  public boolean hasAnnotation( IType type )
  {
    return ANNOTATION_HELPER.hasAnnotation(type, getAnnotations());
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
