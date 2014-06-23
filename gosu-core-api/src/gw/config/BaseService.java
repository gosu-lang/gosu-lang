/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

public abstract class BaseService implements IService
{
  private boolean _inited = false;

  public final boolean isInited()
  {
    return _inited;
  }

  public final void init()
  {
    doInit();
    _inited = true;
  }

  public final void uninit()
  {
    doInit();
    _inited = false;
  }

  protected void doInit() {
    // for subclasses
  }
  
  protected void doUninit() {
    // for subclasses
  }
}