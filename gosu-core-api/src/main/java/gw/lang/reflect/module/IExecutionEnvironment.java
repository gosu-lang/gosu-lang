/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.fs.IResource;
import gw.internal.gosu.parser.TypeSystemState;
import gw.lang.UnstableAPI;

import java.util.List;
import java.net.URL;

@UnstableAPI
public interface IExecutionEnvironment
{
  public final static String GLOBAL_MODULE_NAME = "_globalModule";
  public final static String DEFAULT_SINGLE_MODULE_NAME = "_default_";

  IProject getProject();
  List<? extends IModule> getModules();
  IModule createJreModule( );
  void addModule(IModule module);
  void removeModule(IModule module);
  void renameModule(IModule gosuModule, String name);
  IModule getModule( String strModuleName );
  IModule getModule( IResource file );
  IModule getModule( URL baseURL );
  IModule getGlobalModule();

  IModule getJreModule();

  TypeSystemState getState();

  boolean isShadowingMode();

  String makeGosucProjectFile( String gosucProjectClassName );
}
