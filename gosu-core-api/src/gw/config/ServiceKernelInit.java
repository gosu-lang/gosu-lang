/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

public interface ServiceKernelInit
{
  /**
   * Allows the default kernel services to be overridden, providing
   * customizations at higher levels of the system. 
   */
  public void init( ServiceKernel kernel );

}