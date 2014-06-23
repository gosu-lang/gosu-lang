/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.framework;

public enum MarkerType {
  DELTA_START("[[["),
  DELTA_END("]]]"),
  RANGE_START("[["),
  RANGE_END("]]"),
  CARET1("^^"),
  CARET2("##"),
  CARET3("%%"),
  CARET4("!!"),
  SEPARATOR("~"),
  ;

  public final String markerText;

  MarkerType(String marker) {
    this.markerText = marker;
  }
}
