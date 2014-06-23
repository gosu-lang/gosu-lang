/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.IExpression;

/**
 */
public interface IOptionalParamCapable
{
  /**
   * @return An array of objects representing the default values for a function's parameters.
   *   Returns an empty array if there are no default values, otherwise returns an array coresponing
   *   with default parameter values. If a parameter does not have a default value, it's default value
   *   is null in the array.
   */
  IExpression[] getDefaultValueExpressions();

  String[] getParameterNames();
}
