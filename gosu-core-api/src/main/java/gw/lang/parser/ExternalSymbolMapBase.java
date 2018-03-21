/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.gs.IExternalSymbolMap;

import gw.util.Array;

public abstract class ExternalSymbolMapBase implements IExternalSymbolMap {

  private boolean _assumeSymbolsRequireExternalSymbolMapArgument;

  protected ExternalSymbolMapBase(boolean assumeSymbolsRequireExternalSymbolMapArgument) {
    _assumeSymbolsRequireExternalSymbolMapArgument = assumeSymbolsRequireExternalSymbolMapArgument;
  }

  @Override
  public Object getValue(String name) {
    ISymbol symbol = getSymbol( name );
    verifySymbol( name, symbol );
    // Any external symbols that are properties are coming from the code block, so they implicitly take
    // an IExternalSymbolMap as their first argument
    if (_assumeSymbolsRequireExternalSymbolMapArgument && symbol instanceof IDynamicPropertySymbol) {
      return ((IDynamicPropertySymbol) symbol).getGetterDfs().invoke(new Object[]{this});
    } else {
      return symbol.getValue();
    }
  }

  @Override
  public Object getValue( String name, int iArrayDims ) {
    Object value = getValue( name );
    if( iArrayDims < 0 ) {
      // special case for pcf
      return value;
    }
    if( value == null ) {
      return null;
    }
    Class valueClass = value.getClass();
    if( isCapturedSymbol( iArrayDims, value, valueClass ) ) {
      return ((Object[])value)[0];
    }
    return value;
  }

  private boolean isCapturedSymbol( int iArrayDims, Object value, Class valueClass ) {
    if( value != null && valueClass.isArray() && Array.getLength(value) == 1 ) {
      int iValueDims = 0;
      while( valueClass.isArray() ) {
        iValueDims++;
        valueClass = valueClass.getComponentType();
      }
      if( iValueDims-1 == iArrayDims ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void setValue(String name, Object value) {
    ISymbol symbol = getSymbol( name );
    verifySymbol( name, symbol );
    // Any external symbols that are properties are coming from the code block, so they implicitly take
    // an IExternalSymbolMap as their first argument
    if (_assumeSymbolsRequireExternalSymbolMapArgument && symbol instanceof IDynamicPropertySymbol) {
      ((IDynamicPropertySymbol) symbol).getSetterDfs().invoke(new Object[]{this, value});
    } else {
      symbol.setValue(value);
    }
  }

  @Override
  public Object invoke(String name, Object[] args) {
    IFunctionSymbol functionSymbol = (IFunctionSymbol) getSymbol(name);

    // Any external symbols that are functions are coming from the code block, so they implicitly take
    // an IExternalSymbolMap as their first argument
    if (shouldAddInExternalSymbolMapArgumentForFunctionSymbol(functionSymbol)) {
      // Swap the external symbols map into the first position
      if (args == null) {
        args = new Object[]{this};
      } else {
        Object[] originalArgs = args;
        args = new Object[originalArgs.length + 1];
        args[0] = this;
        System.arraycopy(originalArgs, 0, args, 1, originalArgs.length);
      }
    }

    return functionSymbol.invoke(args);
  }

  protected boolean shouldAddInExternalSymbolMapArgumentForFunctionSymbol(IFunctionSymbol symbol) {
    return _assumeSymbolsRequireExternalSymbolMapArgument;
  }

  protected void verifySymbol( String name, ISymbol symbol ) {
    if( symbol == null ) {
      throw new IllegalStateException( "External symbol not found: " + name );
    }
  }

  protected abstract ISymbol getSymbol(String name);
}