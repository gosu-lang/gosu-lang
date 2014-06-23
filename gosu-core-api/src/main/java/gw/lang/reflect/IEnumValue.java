/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IEnumValue extends IEnumConstant
{
  /**
   * @return The enum constant instance
   */
  public Object getValue();
}