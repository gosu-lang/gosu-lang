/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.javadoc;

import gw.lang.javadoc.IParamNode;
import gw.lang.javadoc.IExceptionNode;

public interface IDocNodeWithParams {

  void addParam( IParamNode paramNode );

  void addException( IExceptionNode exNode );

}
