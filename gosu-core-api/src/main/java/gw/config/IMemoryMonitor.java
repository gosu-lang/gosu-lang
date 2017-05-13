/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.config;

import manifold.api.host.RefreshRequest;
import manifold.api.service.IService;

public interface IMemoryMonitor extends IService
{

  void reclaimMemory(RefreshRequest request);

}
