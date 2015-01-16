package gw.internal.gosu.util;

import gw.util.ILogger;
import gw.util.SystemOutLogger;

/**
 * This class provides default {@code System.out} implementation of {@link GosuLoggerBinder}.
 *
 * <p>
 *   It is instantiated by {@link gw.util.GosuLoggerFactory} if {@code OverrideStaticGosuLoggerBinder}
 *   can not be found.
 * </p>
 *
 * @author isilvestrov
 */
public class DefaultStaticGosuLoggerBinder implements GosuLoggerBinder {
  private static final ILogger DEFAULT_LOGGER = new SystemOutLogger(SystemOutLogger.LoggingLevel.WARN);

  static {
    System.out.println("System.out DefaultStaticGosuLoggerBinder is initialized");
  }

  @Override
  public ILogger getLogger() {
    return DEFAULT_LOGGER;
  }

  // This method must have clazz parameter as slf4j "implementation"
  // supports logging different loggers.
  @Override
  public ILogger getLogger(Class clazz) {
    return DEFAULT_LOGGER;
  }

  // This method must have clazz parameter as slf4j "implementation"
  // supports logging different loggers.
  @Override
  public ILogger getLogger(String name) {
    return DEFAULT_LOGGER;
  }
}
