/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.reflect.IFeatureInfo;

public interface IUsesStatement extends IStatement
{
  String getTypeName();
  boolean isFeatureSpace();
  IFeatureInfo getFeatureInfo();
}
