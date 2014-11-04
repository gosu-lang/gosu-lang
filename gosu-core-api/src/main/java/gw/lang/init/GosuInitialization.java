/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.init;

import gw.lang.UnstableAPI;
import gw.lang.gosuc.GosucModule;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

@UnstableAPI
public class GosuInitialization
{
  private static final Map<IExecutionEnvironment, GosuInitialization> INSTANCES = new WeakHashMap<IExecutionEnvironment, GosuInitialization>();

  private IExecutionEnvironment _execEnv;
  private boolean _initialized;

  public static GosuInitialization instance( IExecutionEnvironment execEnv ) {
    GosuInitialization gi = INSTANCES.get( execEnv );
    if( gi == null ) {
      INSTANCES.put( execEnv, gi = new GosuInitialization( execEnv ) );
    }
    return gi;
  }

  public static boolean isAnythingInitialized() {
    return !INSTANCES.isEmpty();
  }

  private GosuInitialization( IExecutionEnvironment execEnv ) {
    _execEnv = execEnv;
    _initialized = false;
  }

  public boolean isInitialized() {
    return _initialized;
  }

  // single module (i.e. runtime)

  public void uninitializeRuntime() {
    if (!_initialized) {
      return;
    }
    _initialized = false;
    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod("uninitializeRuntime", IExecutionEnvironment.class );
      m.invoke( null, _execEnv );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void initializeRuntime( List<GosuPathEntry> pathEntries ) {
    if (_initialized) {
      throw new IllegalStateException("Illegal attempt to re-initialize Gosu");
    }
    callMethod("initializeRuntime", pathEntries);
    _initialized = true;
  }

  public void initializeCompiler(GosucModule module) {
    if (_initialized) {
      throw new IllegalStateException("Illegal attempt to re-initialize Gosu");
    }
    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod("initializeCompiler", IExecutionEnvironment.class, GosucModule.class);
      m.invoke(null, _execEnv, module);
    } catch (Exception e) {
      throw GosuExceptionUtil.forceThrow( e );
    }
    _initialized = true;
  }

  public void uninitializeCompiler() {
    if (!_initialized) {
      throw new IllegalStateException("Illegal attempt to uninitialize Gosu");
    }
    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod("uninitializeCompiler", IExecutionEnvironment.class );
      m.invoke(null, _execEnv );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    _initialized = false;
  }

  public void reinitializeRuntime( List<GosuPathEntry> pathEntries ) {
    if (_initialized) {
      uninitializeRuntime();
      callMethod("reinitializeRuntime", pathEntries);
    } else {
      callMethod("initializeRuntime", pathEntries);
    }
    _initialized = true;
  }

  // multiple modules

  public void initializeMultipleModules( List<? extends IModule> modules ) {
    if (_initialized) {
      throw new IllegalStateException("Illegal attempt to initialize Gosu");
    }
    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod("initializeMultipleModules", IExecutionEnvironment.class, List.class);
      m.invoke(null, _execEnv, modules);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    _initialized = true;
  }

  public void uninitializeMultipleModules() {
    if (!_initialized) {
      throw new IllegalStateException("Illegal attempt to uninitialize Gosu");
    }
    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod("uninitializeMultipleModules", IExecutionEnvironment.class );
      m.invoke(null, _execEnv );
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    _initialized = true;
  }

  // utilities

  private void callMethod( String methodName, List<GosuPathEntry> pathEntries ) {
    try {
      Class cls = Class.forName("gw.internal.gosu.init.InternalGosuInit");
      Method m = cls.getMethod(methodName, IExecutionEnvironment.class, List.class);
      m.invoke(null, _execEnv, pathEntries);
    } catch (Exception e) {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

}
