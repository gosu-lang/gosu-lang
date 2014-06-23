/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.util;

import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.IType;

import java.util.List;

/**
 */
public class GosuClassTestUtil
{
  public static List<? extends IDynamicFunctionSymbol> getMemberFunctions( IGosuClass clazz )
  {
    return clazz.getMemberFunctions();
  }

  public static ParseResultsException declCompileAndGetClassErrors( IType type )
  {
    assert type instanceof IGosuClass;
    try
    {
      type.getTypeInfo(); // ensures type is decl compiled
    }
    catch( Exception ex )
    {
      // ignore
    }
    return ((IGosuClass)type).getParseResultsException();
  }

  public static ParseResultsException defnCompileAndGetClassErrors( IType type )
  {
    assert type instanceof IGosuClass;
    try
    {
      type.isValid(); // ensures type is defn compiled
    }
    catch( Exception ex )
    {
      // ignore
    }
    return ((IGosuClass)type).getParseResultsException();
  }
}
