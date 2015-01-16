/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.util;

import static gw.util.SystemOutLogger.LoggingLevel.*;


public class SystemOutLogger implements ILogger {

  public enum LoggingLevel {
    TRACE,  //Enable trace and higher
    DEBUG,  //Enable debug and higher
    INFO,   //Enable info and higher
    WARN,   //Enable warn and higher
    ERROR,  //Enable warn and higher
  }

  LoggingLevel _level = WARN;

  public SystemOutLogger() {
  }

  public SystemOutLogger(LoggingLevel level) {
    setLevel(level);
  }

  public void setLevel(LoggingLevel level) {
    _level = level;
  }

  public String getName()
  {
    return "SystemOutLogger";
  }

  public void trace( Object o )
  {
    log(TRACE, o );
  }

  public void trace( Object o, Throwable throwable )
  {
    log(TRACE, o, throwable );
  }

  @Override
  public void trace(String format, Object... arguments) {
    String result = LogMessageFormatter.arrayFormat(format, arguments);
    log(TRACE, result);
  }

  public boolean isTraceEnabled()
  {
    return _level.compareTo(LoggingLevel.TRACE) < 0;
  }

  public void info( Object o, Throwable throwable )
  {
    log(INFO, o, throwable );
  }

  public void debug(Object s) {
    log(DEBUG, s);
  }

  public void debug(Object s, Throwable throwable) {
    log(DEBUG, s, throwable);
  }

  public boolean isDebugEnabled() {
    return _level.compareTo(LoggingLevel.DEBUG) < 0;
  }

  public void info(Object msg) {
    log(INFO, msg);
  }

  public boolean isInfoEnabled() {
    return _level.compareTo(LoggingLevel.INFO) < 0;
  }

  public void warn(Object msg) {
    log(WARN, msg);
  }

  public void warn(Object msg, Throwable throwable) {
    log(WARN, msg, throwable);
  }

  public boolean isWarnEnabled() {
    return _level.compareTo(LoggingLevel.WARN) < 0;
  }

  public void error(Object msg) {
    log(ERROR, msg);
  }

  public void error(Object msg, Throwable throwable) {
    log(ERROR, msg, throwable);
  }

  public boolean isErrorEnabled() {
    return _level.compareTo(LoggingLevel.ERROR) < 0;
  }

  public void fatal(Object msg) {
    log(ERROR, msg);
  }

  public void fatal(Object msg, Throwable throwable) {
    log(ERROR, msg, throwable);
  }

  public boolean isFatalErrorEnabled() {
    return true;
  }

  protected void log(LoggingLevel level, Object msg) {
    log(level, msg, null);
  }

  protected void log(LoggingLevel level, Object msg, Throwable t) {
    if (_level.compareTo(level) <= 0) {
      System.out.println(msg);
      if (t != null) {
        t.printStackTrace();
      }
    }
  }
}
