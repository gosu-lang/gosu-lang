/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.javadoc;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface JavaHasParams {

  public IDocRef<IParamNode> getDocsForParam( int paramIndex );

}
