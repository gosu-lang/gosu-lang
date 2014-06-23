/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.lang.ir.IRSymbol;

import java.util.Collection;
import java.util.HashMap;

public class IRCompilerScope {

  private IRCompilerScope _parent;
  private HashMap<String, IRCompilerLocalVar> _localVars;
  private int _size;
  private boolean _active = true;

  public IRCompilerScope(IRCompilerScope parent) {
    _parent = parent;
    _localVars = new HashMap<String, IRCompilerLocalVar>();
  }

  public IRCompilerLocalVar findLocalVar(IRSymbol symbol) {
    if (_localVars.containsKey(symbol.getName())) {
      return _localVars.get(symbol.getName());
    } else if (_parent != null) {
      return _parent.findLocalVar(symbol);
    } else {
      return null;
    }
  }

  public IRCompilerLocalVar createLocalVar(IRSymbol symbol) {
    IRCompilerLocalVar existingVar = findLocalVar( symbol );
    if (existingVar == null) {
      existingVar = new IRCompilerLocalVar(symbol, totalWidth(), this);
      _localVars.put(symbol.getName(), existingVar);
      _size += existingVar.getWidth();
    }
    return existingVar;
  }

  public Collection<IRCompilerLocalVar> getLocalVars() {
    return _localVars.values();
  }

  public int totalWidth() {
    return (_parent == null ? 0 : _parent.totalWidth()) + _size;
  }

  public boolean isActive() {
    return _active;
  }

  public void scopeRemoved() {
    _active = false;
  }
}
