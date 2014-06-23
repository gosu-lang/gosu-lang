/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRType;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

public class IRScope {
  private Map<String, IRSymbol> _symbols = new HashMap<String, IRSymbol>();
  private IRScope _parent;

  public IRScope(IRScope parent) {
    _parent = parent;
  }

  public void addSymbol(IRSymbol symbol) {
    _symbols.put(symbol.getName(), symbol);
  }

  public IRSymbol addSymbol(String name, IRType type) {
    IRSymbol symbol = new IRSymbol(name, type, false);
    _symbols.put(name, symbol);
    return symbol;
  }

  public IRSymbol getSymbol(String name) {
    IRSymbol localSymbol = _symbols.get(name);
    if (localSymbol != null) {
      return localSymbol;
    } else if (_parent != null) {
      return _parent.getSymbol(name);
    } else {
      return null;
    }
  }

  public Collection<IRSymbol> getSymbols()
  {
    return _symbols.values();
  }
}
