/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import java.util.HashMap;

public class StandardScope<K extends CharSequence, V extends ISymbol>  extends HashMap<K, V>  implements IScope<K, V>
{
  private IActivationContext _activationContext;
  private int _csr;


  public StandardScope()
  {
    super();
  }

  public StandardScope( IActivationContext context )
  {
    this();
    _activationContext = context;
  }

  public StandardScope( int iSize )
  {
    super( iSize );
  }

  public StandardScope( IActivationContext context, int iSize )
  {
    this( iSize );
    _activationContext = context;
  }

  /**
   */
  @Override
  public Object clone()
  {
    StandardScope scope = (StandardScope)super.clone();
    scope._activationContext = _activationContext;
    return scope;
  }

  /**
   */
  public IActivationContext getActivationCtx()
  {
    return _activationContext;
  }

  public int countSymbols()
  {
    return values().size();
  }

  public int getCSR()
  {
    return _csr;
  }

  public void setCSR( int csr )
  {
    _csr = csr;
  }
}
