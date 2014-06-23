/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.debugger.AbstractActivationContext;

/**
 */
public abstract class TransparentActivationContext extends AbstractActivationContext
{
  public TransparentActivationContext( Object ctx )
  {
    super( ctx );
  }

  public boolean isTransparent()
  {
    return true;
  }
}
