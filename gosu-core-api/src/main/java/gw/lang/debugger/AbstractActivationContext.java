/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.debugger;

import gw.lang.parser.IActivationContext;

/**
 * @deprecated
 */
public abstract class AbstractActivationContext implements IActivationContext
{
  private Object _ctx;
  private int _iCurrentScopeIndex;
  private int _iCurrentPrivateGlobalScopeIndex;

  public AbstractActivationContext( Object ctx )
  {
    _ctx = ctx;
  }

  public Object getContext()
  {
    return _ctx;
  }

  public int getCurrentScopeIndex()
  {
    return _iCurrentScopeIndex;
  }

  public void setCurrentScopeIndex( int iIndex )
  {
    _iCurrentScopeIndex = iIndex;
  }

  public int getCurrentPrivateGlobalScopeIndex()
  {
    return _iCurrentPrivateGlobalScopeIndex;
  }

  public void setCurrentPrivateGlobalScopeIndex( int iIndex )
  {
    _iCurrentPrivateGlobalScopeIndex = iIndex;
  }

  public boolean isTransparent()
  {
    return false;
  }
}
