/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.lang.parser.expressions.IPropertyAccessIdentifier;
import gw.util.GosuExceptionUtil;

public class PropertyAccessIdentifier extends Identifier implements IPropertyAccessIdentifier
{
  private final Identifier _identifier;

  public PropertyAccessIdentifier( Identifier identifier )
  {
    _identifier = identifier;
    //copy warnings and errors over
    addParseExceptions( identifier.getParseExceptions() );
    addParseWarnings( identifier.getParseWarnings() );
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
    return _identifier;
  }
}
