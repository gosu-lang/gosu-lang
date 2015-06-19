/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.module.IModule;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class TypeVariableJavaTypeVariable extends TypeJavaClassType implements IJavaClassTypeVariable {
  private TypeVariable _typeParameter;
  private IModule _module;

  public TypeVariableJavaTypeVariable(TypeVariable typeParameter, IModule module) {
    super(typeParameter, module);
    _typeParameter = typeParameter;
    _module = module;
  }

  @Override
  public IJavaClassType getConcreteType() {
    throw new RuntimeException("if called, fix it");
  }

  @Override
  public String getName() {
    return _typeParameter.getName();
  }

  @Override
  public String getSimpleName() {
    return getName();
  }

  @Override
  public IJavaClassType[] getBounds() {
    Type[] rawBounds = _typeParameter.getBounds();
    IJavaClassType[] bounds = new IJavaClassType[rawBounds.length];
    for (int i = 0; i < bounds.length; i++) {
      bounds[i] = TypeJavaClassType.createType(rawBounds[i], _module);
    }
    return bounds;
  }

  @Override
  public boolean isFunctionTypeVar() {
    return _typeParameter.getGenericDeclaration() instanceof Method;
  }
}