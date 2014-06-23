/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.config.IService;
import gw.util.ILogger;
import gw.util.SystemOutLogger;

/**
 * This interface deals with getting a logger for the given category or
 * a logger that follows a standard logging policy.
 *
 * @author dandrews
 */
public interface IXmlLoggerFactory extends IService {

  /* these categories need to be defined in PLLoggerCategory as well */
  public enum Category { Loading, Runtime, XmlMarshal, XmlUnMarshal, Request }

  /** This will get the logger for the specific type
   *
   * @param category the category needed
   * @return the logger
   */
  ILogger getLogger(Category category);


  /** This will get the logger for the specific type
   *
   * @param category the category needed
   * @return the logger
   */
  ILogger getLogger(String category);

  /** This will get the logger for any custom category, it will also initialize it to
   * a console appender at warning level if not otherwise configured.
   *
   * @param category the category needed
   * @param level the level to set this temporary logger to
   * @return the logger
   */
  public ILogger getTempLogger(String category, SystemOutLogger.LoggingLevel level);

}
