/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.ir.IRType;

public interface IRProperty {

  IRType getType();

  IRType getAssignableType();

  String getName();

  boolean isField();

  boolean isCaptured();

  IRMethod getGetterMethod();

  IRMethod getSetterMethod();

  IRType getOwningIRType();

  IType getOwningIType();

  IRelativeTypeInfo.Accessibility getAccessibility();

  boolean isStatic();

  IRType getTargetRootIRType( );

  boolean isBytecodeProperty();
}
