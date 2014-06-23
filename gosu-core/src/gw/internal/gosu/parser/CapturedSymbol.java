/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IStackProvider;
import gw.lang.parser.ISymbol;
import gw.lang.parser.IScope;
import gw.lang.parser.ICapturedSymbol;

/**
 * A symbol that has been captured from an outer lexical scope
 */
public class CapturedSymbol extends Symbol implements ICapturedSymbol
{
  private ISymbol _referredSymbol;

  public CapturedSymbol( String strName, ISymbol sym, IStackProvider stackProvider, IScope scope )
  {
    super( strName, sym.getType(), stackProvider, null, scope );
    _referredSymbol = sym;
    sym.setValueIsBoxed( true ); //the parent symbol should now use a reference
    this.setValueIsBoxed( true ); //captured symbols always use references
  }

  public ISymbol getReferredSymbol() {
    return _referredSymbol;
  }

  public ISymbol getLightWeightReference()
  {
    return this;
  }

  @Override
  public boolean isLocal() {
    return _referredSymbol.isLocal();
  }

  @Override
  public boolean isWritable() {
    return _referredSymbol.isWritable();
  }
}