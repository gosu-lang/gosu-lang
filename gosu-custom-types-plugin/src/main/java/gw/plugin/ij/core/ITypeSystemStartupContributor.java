/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.project.Project;

public interface ITypeSystemStartupContributor {
  String accepts( Project project );
  void beforeTypesystemStartup( Project project ) throws Exception;
  void afterTypesystemStartup( Project project ) throws Exception;
  void afterPluginShutdown( Project project ) throws Exception;
}
