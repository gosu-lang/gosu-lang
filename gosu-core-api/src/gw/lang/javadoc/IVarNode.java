/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.javadoc;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IVarNode extends IBaseFeatureNode {

  String getName();

  void setName( String name );

}
