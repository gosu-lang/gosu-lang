/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

public enum PluginFailureReason {
  NONE,
  NO_SDK,
  INVALID_SDK,
  CIRCULAR_DEPENDENCY,
  MULTIPLE_PROJECTS_OPEN,
  EXCEPTION,
}
