/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.Collections;
import java.util.List;

public class FunctionTypeInfo extends BaseFeatureInfo implements ITypeInfo
{
  public FunctionTypeInfo( IFunctionType functionType )
  {
    super( functionType );
  }

  public List<? extends IPropertyInfo> getProperties()
  {
    return Collections.emptyList();
  }

  public IPropertyInfo getProperty( CharSequence property )
  {
    return null;
  }

  public MethodList getMethods()
  {
    return MethodList.EMPTY;
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return FIND.method( getMethods(), methodName, params );
  }

  public String getName()
  {
    return getOwnersType().getName();
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public String getDisplayName()
  {
    return getOwnersType().getRelativeName();
  }

  public String getShortDescription()
  {
    return getOwnersType().getRelativeName();
  }

  public String getDescription()
  {
    return null;
  }

  public boolean isStatic()
  {
    return false;
  }

  public List<IConstructorInfo> getConstructors()
  {
    return Collections.emptyList();
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return null;
  }

  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return FIND.callableMethod( getMethods(), strMethod, params );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return FIND.callableConstructor( getConstructors(), params );
  }

  public List<IEventInfo> getEvents()
  {
    return Collections.emptyList();
  }

  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }
}
