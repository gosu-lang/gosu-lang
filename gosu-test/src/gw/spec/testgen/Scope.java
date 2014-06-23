/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.testgen;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:03:52 AM
 * To change this template use File | Settings | File Templates.
 */
public enum Scope {
  Static("static "), Instance("");

  private String _scopeQualifier;

  private Scope(String scopeQualifier) {
    _scopeQualifier = scopeQualifier;
  }

  public String getScopeQualifier() {
    return _scopeQualifier;
  }
}
