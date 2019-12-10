/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import com.sun.source.tree.Tree;
import gw.lang.reflect.java.IJavaClassGenericArrayType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.asm.IAsmType;

public class AsmGenericArrayTypeJavaClassGenericArrayType extends AsmTypeJavaClassType implements IJavaClassGenericArrayType {

  public AsmGenericArrayTypeJavaClassGenericArrayType( IAsmType genericArrayType ) {
    super(genericArrayType );
  }

  @Override
  public IJavaClassType getGenericComponentType() {
    return AsmTypeJavaClassType.createType( getType().getComponentType() );
  }

  @Override
  public IJavaClassType getConcreteType() {
    return null;
  }

  @Override
  public String getSimpleName() {
    return getType().getSimpleName();
  }

  @Override
  public Tree getTree()
  {
    return null;
  }

  @Override
  public IJavaClassInfo getEnclosingClass()
  {
    return null;
  }

  @Override
  public boolean isArray() {
    return true;
  }

  @Override
  public IJavaClassType getComponentType() {
    return getGenericComponentType();
  }
}
