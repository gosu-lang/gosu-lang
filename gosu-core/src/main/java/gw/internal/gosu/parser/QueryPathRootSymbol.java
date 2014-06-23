/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IType;

/**
 * @deprecated
 */
public class QueryPathRootSymbol extends Symbol
{
  public QueryPathRootSymbol( String strName, IType type, Object value )
  {
    super( strName, type, null, value );
  }

}
