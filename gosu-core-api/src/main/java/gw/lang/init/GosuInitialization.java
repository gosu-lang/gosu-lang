/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.init;

import gw.lang.UnstableAPI;
import gw.lang.gosuc.GosucModule;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.util.GosuExceptionUtil;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import manifold.internal.javac.JavacPlugin;

@UnstableAPI
public class GosuInitialization
{
  private static final Map<IExecutionEnvironment, GosuInitialization> INSTANCES = new WeakHashMap<>();

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
    getGosuInitialization().uninitializeRuntime( _execEnv );
  }

  public void uninitializeSimpleIde() {
    if (!_initialized) {
      return;
    }
    _initialized = false;
    getGosuInitialization().uninitializeSimpleIde( _execEnv );
  }

  public void initializeRuntime( List<GosuPathEntry> pathEntries, String... discretePackages ) {
    if (_initialized) {
      throw new IllegalStateException("Illegal attempt to re-initialize Gosu");
    }
    getGosuInitialization().initializeRuntime( _execEnv, pathEntries, discretePackages );
    _initialized = true;
  }

  public void initializeCompiler(GosucModule module) {
    if (_initialized) {
      throw new IllegalStateException("Illegal attempt to re-initialize Gosu");
    }
    getGosuInitialization().initializeCompiler( _execEnv, module );
    _initialized = true;

    if( JavacPlugin.instance() == null )
    {
      GosuRuntimeManifoldHost.clear();
      GosuRuntimeManifoldHost.get().init( module.getAllSourceRoots().stream().map( makeFile() ).collect( Collectors.toList() ),
        module.getClasspath().stream().filter( p -> !p.startsWith( "jrt:" ) ).map( makeFile() ).collect( Collectors.toList() ) );
    }
  }

  private Function<String, File> makeFile()
  {
    return name -> name.startsWith( "file:" ) ? new File( URI.create( name ) ) : new File( name );
  }

  public void uninitializeCompiler() {
    if (!_initialized) {
      throw new IllegalStateException("Illegal attempt to uninitialize Gosu");
    }
    getGosuInitialization().uninitializeCompiler( _execEnv );
    _initialized = false;
  }

  public void reinitializeRuntime( List<GosuPathEntry> pathEntries, String... discretePackages ) {
    if (_initialized) {
      uninitializeRuntime();
      getGosuInitialization().reinitializeRuntime( _execEnv, pathEntries, discretePackages );
    } else {
      getGosuInitialization().initializeRuntime( _execEnv, pathEntries, discretePackages );
    }
    _initialized = true;
  }

  public void reinitializeSimpleIde( GosucModule module ) {
    if (_initialized) {
      uninitializeSimpleIde();
      getGosuInitialization().reinitializeSimpleIde( _execEnv, module );
    } else {
      getGosuInitialization().initializeSimpleIde( _execEnv, module );
    }
    _initialized = true;
  }

  // multiple modules

  public void initializeMultipleModules( List<? extends IModule> modules ) {
    if (_initialized) {
      throw new IllegalStateException("Illegal attempt to initialize Gosu");
    }
    getGosuInitialization().initializeMultipleModules( _execEnv, modules );
    _initialized = true;
  }

  public void uninitializeMultipleModules() {
    if (!_initialized) {
      throw new IllegalStateException("Illegal attempt to uninitialize Gosu");
    }
    getGosuInitialization().uninitializeMultipleModules( _execEnv );
    _initialized = true;
  }

  // utilities

  private IGosuInitialization getGosuInitialization() {
    try {
      Class<?> cls = Class.forName( "gw.internal.gosu.init.InternalGosuInit" );
      Method m = cls.getMethod( "instance" );
      return (IGosuInitialization)m.invoke( null );
    }
    catch( Exception e ) {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

}
