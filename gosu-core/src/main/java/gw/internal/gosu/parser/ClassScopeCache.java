/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IScope;

import java.util.List;
import java.util.Map;

public class ClassScopeCache
{
  private final IScope _staticScope;
  private final Map<String, List<IFunctionSymbol>> _staticDfsMap;
  private final IScope _nonstaticScope;
  private final Map<String, List<IFunctionSymbol>> _nonstaticDfsMap;

  public ClassScopeCache( IScope staticScope, Map<String, List<IFunctionSymbol>> staticDfsMap, IScope nonstaticScope, Map<String, List<IFunctionSymbol>> nonstaticDfsMap )
  {
    _staticScope = staticScope;
    _staticDfsMap = staticDfsMap;
    _nonstaticScope = nonstaticScope;
    _nonstaticDfsMap = nonstaticDfsMap;
  }

  public IScope getStaticScope()
  {
    return _staticScope;
  }

  public Map<String, List<IFunctionSymbol>> getStaticDfsMap()
  {
    return _staticDfsMap;
  }

  public IScope getNonstaticScope()
  {
    return _nonstaticScope;
  }

  public Map<String, List<IFunctionSymbol>> getNonstaticDfsMap()
  {
    return _nonstaticDfsMap;
  }
}
