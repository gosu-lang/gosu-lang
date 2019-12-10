package gw.lang.init;

import gw.lang.gosuc.GosucModule;
import gw.lang.reflect.module.IExecutionEnvironment;

import java.util.List;

/**
 */
public interface IGosuInitialization
{
  void initializeRuntime( IExecutionEnvironment execEnv, List<? extends GosuPathEntry> pathEntries, String... discretePackages );
  void reinitializeRuntime( IExecutionEnvironment execEnv, List<? extends GosuPathEntry> pathEntries, String... discretePackages );
  void uninitializeRuntime( IExecutionEnvironment execEnv );

  void initializeCompiler( IExecutionEnvironment execEnv, GosucModule module );
  void uninitializeCompiler( IExecutionEnvironment execEnv );

  void initializeSimpleIde( IExecutionEnvironment execEnv, GosucModule module );
  void reinitializeSimpleIde( IExecutionEnvironment execEnv, GosucModule module );
  void uninitializeSimpleIde( IExecutionEnvironment execEnv );
}

