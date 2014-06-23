/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.parser.ICoercer;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public abstract class BaseCoercer implements ICoercer
{
  public BaseCoercer()
  {
    Class<? extends BaseCoercer> thisClass = this.getClass();
    if( !thisClass.getName().endsWith( "Coercer" ) )
    {
      throw new IllegalStateException( "All coercers must have a name ending with 'Coercer': " + this.getClass().getName() );
    }
    if (!(this instanceof BasePrimitiveCoercer)) {
      Method method = null;
      try
      {
        method = thisClass.getMethod( "instance" );
        if( !Modifier.isStatic( method.getModifiers() ) || !method.getReturnType().equals( thisClass ) )
        {
          method = null;
        }
      }
      catch( NoSuchMethodException e )
      {
        //dealt with below
      }
      if( method == null )
      {
        throw new IllegalStateException( "This coercer must be a singleton that has a static method 'instance' with the return type " + thisClass.getName() );
      }
    }
  }
}
