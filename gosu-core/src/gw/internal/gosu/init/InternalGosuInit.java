/*
 * Copyright 2014 Guidewire Software, Inc.
 */
package gw.internal.gosu.init;

import gw.internal.gosu.parser.ExecutionEnvironment;
import gw.internal.gosu.parser.TypeLoaderAccess;
import gw.lang.init.GosuPathEntry;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;

import java.util.List;

public class InternalGosuInit {

  // single module (i.e. runtime)

  public static void initializeRuntime( IExecutionEnvironment execEnv, List<? extends GosuPathEntry> pathEntries ) {
    ((ExecutionEnvironment)execEnv).initializeDefaultSingleModule(pathEntries);
  }

  public static void reinitializeRuntime( IExecutionEnvironment execEnv, List<? extends GosuPathEntry> pathEntries ) {
    ((ExecutionEnvironment)execEnv).initializeDefaultSingleModule( pathEntries );
    TypeLoaderAccess.instance().incrementChecksums();
  }

  public static void uninitializeRuntime( IExecutionEnvironment execEnv ) {
    ((ExecutionEnvironment)execEnv).uninitializeDefaultSingleModule();
  }

  // multiple modules

  public static void initializeMultipleModules( IExecutionEnvironment execEnv, List<? extends IModule> modules ) {
    ((ExecutionEnvironment)execEnv).initializeMultipleModules( modules );
  }

  public static void uninitializeMultipleModules( IExecutionEnvironment execEnv ) {
    if (execEnv.isSingleModuleMode()) {
      throw new IllegalStateException( "The typestem is not in multi-module mode." );
    }
    ((ExecutionEnvironment)execEnv).uninitializeMultipleModules();
  }
}
