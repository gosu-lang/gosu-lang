/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.Collections;
import java.util.List;

public abstract class BaseJavaTypeInfo extends BaseFeatureInfo implements ITypeInfo
{
  public BaseJavaTypeInfo( Class javaClass )
  {
    //super( TypeSystem.get( javaClass ) );
    super( TypeSystem.getByFullName( javaClass.getName() ) );
  }

  public String getName()
  {
    return getOwnersType().getRelativeName();
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getShortDescription()
  {
    return getName();
  }

  public String getDescription()
  {
    return getName();
  }

  public boolean isStatic()
  {
    return true;
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    return FIND.method( getMethods(), methodName, params );
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return FIND.constructor( getConstructors(), params );
  }

  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    return FIND.callableMethod( getMethods(), strMethod, params );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    return FIND.callableConstructor( getConstructors(), params );
  }

}
