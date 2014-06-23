/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.javadoc;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IParamNode {

  String getDescription();

  void setDescription( String value );

  String getName();

  void setName( String name );

  String getType();

  void setType( String name );

}
