/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IRType {

  String getName();

  String getRelativeName();

  String getDescriptor();

  // Note:  This method should ONLY be called AFTER compilation
  Class getJavaClass();

  String getSlashName();

  IRType getArrayType();

  IRType getComponentType();

  boolean isArray();

  boolean isAssignableFrom( IRType otherType );

  boolean isByte();

  boolean isBoolean();

  boolean isShort();

  boolean isChar();

  boolean isInt();

  boolean isLong();

  boolean isFloat();

  boolean isDouble();

  boolean isVoid();

  boolean isPrimitive();

  boolean isInterface();

  boolean isStructural();

  boolean isStructuralAndErased( IRType ownersType );
}
