/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.memory;

import gw.config.IMemoryMonitor;
import manifold.api.host.RefreshRequest;
import manifold.api.service.BaseService;

public class DefaultMemoryMonitor extends BaseService implements IMemoryMonitor {

  @Override
  public void reclaimMemory(RefreshRequest request) {
//    if( request == null ) {
//      return;
//    }
//    if( request.kind == RefreshKind.MODIFICATION ) {
//      List<String> combined = new ArrayList<String>( request.types );
//      for( IModule module: request.module.getModuleTraversalList() ) {
//        List<ITypeRef> oldTypes = module.getModuleTypeLoader().getTypeRefFactory().huntForTypesToKill( request.isLimitlessHunt() );
//        combined.addAll( oldTypes );
//        request.setTypes( combined );
//      }
//    }
  }

}
