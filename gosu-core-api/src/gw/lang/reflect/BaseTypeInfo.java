/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.Collections;
import java.util.List;

public class BaseTypeInfo extends BaseFeatureInfo implements ITypeInfo
{
  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public BaseTypeInfo( IType type )
  {
    super( type );
  }

  public boolean isStatic()
  {
    return false;
  }

  public String getName()
  {
    return getOwnersType().getRelativeName();
  }

  public List<? extends IPropertyInfo> getProperties()
  {
    return Collections.emptyList();
  }

  public IPropertyInfo getProperty( CharSequence propName )
  {
    return null;
  }

  public MethodList getMethods()
  {
    return MethodList.EMPTY;
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return null;
  }

  public IMethodInfo getCallableMethod( CharSequence method, IType... params )
  {
    return null;
  }

  public List<? extends IConstructorInfo> getConstructors()
  {
    return Collections.emptyList();
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return null;
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return null;
  }

  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  public IEventInfo getEvent( CharSequence event )
  {
    return null;
  }

}
