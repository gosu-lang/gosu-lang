/*
 * Copyright 2014. Guidewire Software, Inc.
 */

package gw.lang.reflect;

/**
 */
public interface IDynamicType extends IType, IPlaceholder {
  String PKG = "dynamic";
  String RNAME = "Dynamic";
  String QNAME = PKG +'.'+ RNAME;

  static IDynamicType instance() {
    return (IDynamicType)TypeSystem.getByFullName( QNAME );
  }
}
