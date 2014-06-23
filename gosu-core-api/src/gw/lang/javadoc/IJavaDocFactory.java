/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.javadoc;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IJavaDocFactory {

  IClassDocNode create( Class javaClass );

  IClassDocNode create();

  /**
   * @return a new, empty IParamNode
   * @deprecated Please don't create these manually, and please fix any code that does.
   */
  @Deprecated
  IParamNode createParam();

  /**
   * @return a new, empty IExceptionNode
   * @deprecated Please don't create these manually, and please fix any code that does.
   */
  @Deprecated
  IExceptionNode createException();

}
