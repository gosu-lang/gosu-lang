/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;

public class TypeVariableJavaClassTypeVariable extends TypeJavaClassType implements IJavaClassTypeVariable {
  private TypeVariable _typeVariable;

  public TypeVariableJavaClassTypeVariable(TypeVariable typeVariable, IModule module) {
    super(typeVariable, module);
    _typeVariable = typeVariable;
  }

  @Override
  public IJavaClassType getConcreteType() {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public String getName() {
    return _typeVariable.getName();
  }

  @Override
  public String getSimpleName() {
    return getName();
  }

  @Override
  public IJavaClassType[] getBounds() {
    return new IJavaClassType[0];  //To change body of implemented methods use File | Settings | File Templates.
  }

  @Override
  public boolean isFunctionTypeVar() {
    return _typeVariable.getGenericDeclaration() instanceof Method;
  }

  @Override
  public IModule getModule() {
    return _module;
  }

  public String toString() {
    return getName();
  }
}