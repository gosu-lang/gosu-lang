/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import java.util.List;

/**
 */
public interface IAsmType {
  String getName();

  String getSimpleName();

  List<AsmType> getTypeParameters();

  boolean isParameterized();

  boolean isArray();

  boolean isTypeVariable();

  boolean isPrimitive();

  String getFqn();

  IAsmType getRawType();

  IAsmType getComponentType();
}
