/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;

import java.util.List;

public interface IHasInnerClass
{
  /**
   * returns the appropriate inner class
   * @param strTypeName
   * @return
   */
  IType getInnerClass( CharSequence strTypeName );

  List<? extends IType> getInnerClasses();

  List<? extends IType> getLoadedInnerClasses();

  IType resolveRelativeInnerClass( String strTypeName, boolean bForce );
}
