/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

public interface IGosuArrayClassInstance extends IGosuObject
{
  int getLength();

  IGosuObject getArrayComponent( int iIndex );

  void setArrayComponent( int iIndex, IGosuObject value );

  IGosuObject[] getObjectArray();

  Object[] toArray();
}
