/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.Collections;
import java.util.List;

public abstract class TypeInfoBase implements ITypeInfo
{
  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return FIND.method( getMethods(), methodName, params );
  }

  public IMethodInfo getCallableMethod( CharSequence method, IType... params )
  {
    return FIND.callableMethod( getMethods(), method, params );
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return FIND.constructor( getConstructors(), params );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return FIND.callableConstructor( getConstructors(), params );
  }

  public List<? extends IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  public IEventInfo getEvent( CharSequence event )
  {
    return null;
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    return Collections.emptyList();
  }

  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return null;
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return null;
  }

  @Override
  public boolean hasAnnotation( IType type )
  {
    return false;
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return false;
  }

  public boolean isDeprecated() {
    return false;
  }

  public String getDeprecatedReason() {
    return null;
  }

  @Override
  public boolean isDefaultImpl() {
    return false;
  }

  public IFeatureInfo getContainer()
  {
    return null;
  }

  public String getName()
  {
    return getOwnersType().getName();
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
