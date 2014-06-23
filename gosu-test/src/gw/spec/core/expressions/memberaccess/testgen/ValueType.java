/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.spec.core.expressions.memberaccess.testgen;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 29, 2009
 * Time: 10:04:24 AM
 * To change this template use File | Settings | File Templates.
 */
enum ValueType {
  String("String"), Int("int");

  private String _name;

  private ValueType(String name) {
    _name = name;
  }

  public String getName() {
    return _name;
  }
}
