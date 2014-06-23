/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IType;
import gw.lang.parser.IStackProvider;
import gw.lang.parser.ISymbol;

/**
 */
public class ReadOnlySymbol extends Symbol
{
  public ReadOnlySymbol( String strName, IType type, IStackProvider provider, Object value )
  {
    super( strName, type, provider, value );
  }

  public ISymbol getLightWeightReference()
  {
    return new ReadOnlySymbol( getName(), getType(), _stackProvider, _value );
  }

  public boolean isWritable()
  {
    return false;
  }
}
