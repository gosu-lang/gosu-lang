package gw.util;

import gw.internal.gosu.util.DefaultStaticGosuLoggerBinder;
import gw.internal.gosu.util.GosuLoggerBinder;

/**
 * This class is an API to create implementation independent instances of {@link ILogger} for Gosu.
 *
 * <p>
 *   It is designed to delegate logging to SLF4J (or another logging system) in appropriate environments, but
 *   logs to System.out by default.
 * </p>
 * <p>
 *   The delegation mechanism is somewhat similar to the one that SLF4J has. It selects logger implementation
 *   according to the classes present in the class path. First, it tries to instantiate
 *   {@code OverrideStaticGosuLoggerProvider} (see {@link #OVERRIDE_STATIC_LOGGER_BINDER_CLASS_NAME}
 *   with reflection.
 *   It falls back to instantiate {@link gw.internal.gosu.util.DefaultStaticGosuLoggerBinder}
 *   with {@code new} operator if the "override static provider" is not found.
 * </p>
 * <p>
 *   {@code OverrideStaticGosuLoggerProvider} implemented in Platform delegates to SLF4J.
 *   {@link gw.internal.gosu.util.DefaultStaticGosuLoggerBinder} returns logger that output
 *   to {@code System.out}.
 * </p>
 * <p>
 *   This solution is designed to solve the very narrow problem: to delegate Gosu logging to
 *   SLF4J in Guidewire Platform and Core apps. This is why the implemented approach is different.
 *   SLF4J tries to load only one class &mdash; {@code StaticLoggerBinder}. {@code slf4j-api}
 *   library lacks this class but one of the "impl" libraries must provide it. There must be
 *   only one class with this name the class path. We don't want to have "logger implementation"
 *   libraries for Gosu. It would be a library of one class. We don't also want to make
 *   Gosu API build procedure be more complex. SLF4J removes "binder" class from the jar file
 *   when it builds {@code slf4j-api}. This is why two "binder" classes are searched in
 *   the class path.
 * </p>
 *
 * @see gw.internal.gosu.util.DefaultStaticGosuLoggerBinder
 * @author isilvestrov
 */
public final class GosuLoggerFactory {

  private static final String OVERRIDE_STATIC_LOGGER_BINDER_CLASS_NAME =
          "gw.internal.gosu.util.OverrideStaticGosuLoggerBinder";

  private static final GosuLoggerBinder LOGGER_BINDER;

  static {
    GosuLoggerBinder provider = null;
    try {
      provider = (GosuLoggerBinder)
              Class.forName(OVERRIDE_STATIC_LOGGER_BINDER_CLASS_NAME).newInstance();
    } catch (ClassNotFoundException e) {
      // skip
    } catch (InstantiationException e) {
      // skip
    } catch (IllegalAccessException e) {
      // skip
    }
    if (provider == null)
      provider = new DefaultStaticGosuLoggerBinder();
    LOGGER_BINDER = provider;
  }

  public static ILogger getLogger() {
    return LOGGER_BINDER.getLogger();
  }

  public static ILogger getLogger(Class clazz) {
    return LOGGER_BINDER.getLogger(clazz);
  }

  public static ILogger getLogger(String name) {
    return LOGGER_BINDER.getLogger(name);
  }
}
