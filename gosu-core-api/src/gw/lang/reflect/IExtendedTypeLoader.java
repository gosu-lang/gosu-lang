/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IExtendedTypeLoader extends ITypeLoader
{
  /**
   * Returns the intrinsic type for the given Object.
   *
   * @param object the object to get an IType for
   *
   * @return the IType for the object
   */
  public IType getIntrinsicTypeFromObject( Object object );
}