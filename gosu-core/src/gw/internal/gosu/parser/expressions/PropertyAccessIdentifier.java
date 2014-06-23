/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IPropertyAccessIdentifier;
import gw.util.GosuExceptionUtil;

public class PropertyAccessIdentifier extends Identifier implements IPropertyAccessIdentifier
{
  private final Identifier e;

  public PropertyAccessIdentifier( Identifier e )
  {
    this.e = e;
    //copy warnings and errors over
    this.addParseExceptions( e.getParseExceptions() );
    this.addParseWarnings( e.getParseWarnings() );
  }

  @Override
  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    try
    {
      return super.evaluate();
    }
    catch( Throwable t )
    {
      throw GosuExceptionUtil.forceThrow( t );
    }
  }

  public Identifier getWrappedIdentifier() {
    return e;
  }
}
