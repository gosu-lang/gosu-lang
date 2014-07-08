package gw.internal.gosu.util;

import gw.util.ILogger;

/**
 * Interface to bind different implementations of Gosu Logger.
 *
 * <p>
 *   Implementation is selected and instantiated by {@link gw.util.GosuLoggerFactory}
 * </p>
 * @author isilvestrov
 */
public interface GosuLoggerBinder {
  ILogger getLogger();

  ILogger getLogger(Class clazz);

  ILogger getLogger(String name);
}
