/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.module.IModule;

public interface IFileContext
{
  /**
   * @return Some additional contextual information about the place within a file that this program is located (e.g. an attribute in an XML file)
   */
  String getContextString();

  /**
   * @return a class name that uniquely represents this file context when combined with the context string above.
   */
  String getClassName();

  /**
   * @return the full path
   */
  String getFilePath();
}
