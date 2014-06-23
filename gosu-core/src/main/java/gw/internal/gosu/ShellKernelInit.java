/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu;

import gw.config.ServiceKernelInit;
import gw.config.ServiceKernel;
import gw.lang.reflect.IEntityAccess;

public class ShellKernelInit implements ServiceKernelInit
{
  public void init( ServiceKernel services )
  {
    services.redefineService( IEntityAccess.class, new ShellEntityAccess() );
  }

}
