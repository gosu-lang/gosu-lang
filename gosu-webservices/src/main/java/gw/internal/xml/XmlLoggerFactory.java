/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.xml;

import gw.config.BaseService;
import gw.util.ILogger;
import gw.util.SystemOutLogger;

/**
 * This is logging for the web services module.  Note that CXF required apache commons logging so that package is used.
 * If not configured, it will create a warning level console appender for log4j.
 *
 * @author dandrews
 */
public class XmlLoggerFactory extends BaseService implements IXmlLoggerFactory {

  /** This will get the logger for the specific type
   *
   * @param category the category needed
   * @return the logger
   */
  public ILogger getLogger(Category category) {
    return new SystemOutLogger();
  }

  /** This will get the logger for any custom category, it will also initialize it to
   * a console appender at warning level if not otherwise configured.
   *
   * @param category the category needed
   * @return the logger
   */
  public ILogger getLogger(String category) {
    return new SystemOutLogger();
  }

  /** This will get the logger for any custom category, it will also initialize it to
   * a console appender at warning level if not otherwise configured.
   *
   * @param category the category needed
   * @param level the level to set this temporary logger to
   * @return the logger
   */
  public ILogger getTempLogger(String category, SystemOutLogger.LoggingLevel level) {
    return new SystemOutLogger( level );
  }

}
