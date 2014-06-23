/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaBackedType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.internal.gosu.parser.ClassJavaClassInfo;

import java.util.Set;
import java.util.HashSet;

public class WrappingJavaBackedTestType extends WrappingTestTypeBase implements IJavaBackedType, IWrappingTestType {

  private Class _implCLass;
  private Class _javaSuperClass;

  public WrappingJavaBackedTestType(ITypeLoader loader, String name, IType wrappedType,
                                    Class implClass,
                                    Class javaSuperClass,
                                    Class... wrappedInterfaces) {
    _implCLass = implClass;
    _javaSuperClass = javaSuperClass;
    init(loader, name, wrappedType, wrappedInterfaces);
  }

  @Override
  protected ITypeInfo makeTypeInfo() {
    return new WrappingJavaBackedTestTypeInfo(this, getWrappedType());
  }

  public Class getImplClass() {
    return _implCLass;
  }

  @Override
  protected Set<IType> makeTypeHierarchy() {
    HashSet<IType> types = new HashSet<IType>();
    types.add(this);
    types.addAll(TypeSystem.get(_javaSuperClass).getAllTypesInHierarchy());
    for (Class wrappedInterface : getWrappedInterfaces()) {
      types.add(TypeSystem.get(wrappedInterface));
    }
    return types;
  }

  @Override
  public IType getSupertype() {
    return TypeSystem.get(_javaSuperClass);
  }

  @Override
  public Class getBackingClass() {
    return _implCLass;
  }

  @Override
  public IJavaClassInfo getBackingClassInfo() {
    return new ClassJavaClassInfo(_implCLass, getTypeLoader().getModule());
  }

  @Override
  public IType getTypeFromJavaBackedType() {
    return TypeSystem.getTypeFromJavaBasedType(this);
  }
  
}