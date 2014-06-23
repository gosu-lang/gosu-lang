/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.ir.statement.IRFieldDecl;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.ir.IRType;
import gw.lang.ir.IRSymbol;
import gw.lang.UnstableAPI;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

@UnstableAPI
public class IRBuilderContext {

  private IRMethodBuilder _methodBuilder;
  private Map<String, IRSymbol> _symbols;

  public IRBuilderContext(IRMethodBuilder methodBuilder) {
    _methodBuilder = methodBuilder;

    _symbols = new HashMap<String, IRSymbol>();
    for (IRSymbol symbol : _methodBuilder.getParameters()) {
      _symbols.put( symbol.getName(), symbol );
    }
  }

  public IRFieldDecl findField( String name ) {
    List<IRFieldDecl> fieldDecls = _methodBuilder.getClassBuilder().getFields();
    IRFieldDecl field = null;
    for (IRFieldDecl decl : fieldDecls) {
      if (decl.getName().equals(name)) {
        field = decl;
        break;
      }
    }
    if (field == null) {
      throw new IllegalArgumentException("No field found named " + name);
    }
    return field;
  }

  public IRMethodStatement findMethod( String name, int numArgs ) {
    List<IRMethodStatement> methodDecls = _methodBuilder.getClassBuilder().getMethods();
    IRMethodStatement method = null;
    for (IRMethodStatement decl : methodDecls) {
      if (decl.getName().equals(name) && decl.getParameters().size() == numArgs) {
        if (method != null) {
          throw new IllegalArgumentException("Multiple methods named " + name + " found with " + numArgs + " on type " + owningType().getName());
        } else {
          method = decl;
        }
      }
    }

    return method;
  }

  public IRType owningType() {
    return _methodBuilder.getClassBuilder().getThisType();
  }

  public IRType currentClassSuperType() {
    return _methodBuilder.getClassBuilder().getSuperType();
  }

  public IRType currentReturnType() {
    return _methodBuilder.getReturnType();
  }

  public IRSymbol findVar(String name) {
    IRSymbol symbol = _symbols.get(name);
    if ( symbol == null ) {
      throw new IllegalArgumentException("No symbol found named " + name);
    }
    return symbol;
  }

  public IRSymbol getOrCreateVar(String name, IRType type) {
    IRSymbol symbol = _symbols.get(name);
    if ( symbol == null ) {
      symbol = new IRSymbol( name, type, false );
      _symbols.put( name, symbol );
    }
    return symbol;
  }

  public IRSymbol tempSymbol(IRType type) {
    IRSymbol temp = new IRSymbol( "**temp" + _symbols.size(), type, true );
    _symbols.put( temp.getName(), temp );
    return temp;
  }
}
