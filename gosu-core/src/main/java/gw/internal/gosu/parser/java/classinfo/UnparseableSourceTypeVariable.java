/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java.classinfo;

import gw.lang.parser.TypeVarToTypeMap;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.ITypeInfoResolver;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

public class UnparseableSourceTypeVariable implements IJavaClassTypeVariable {
  private ITypeInfoResolver _owner;

  public UnparseableSourceTypeVariable(ITypeInfoResolver owner) {
    _owner = owner;
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap) {
    return JavaTypes.OBJECT();
  }

  @Override
  public IType getActualType(TypeVarToTypeMap typeMap, boolean bKeepTypeVars) {
    return JavaTypes.OBJECT();
  }

  @Override
  public IJavaClassType getConcreteType() {
    return JavaTypes.OBJECT().getBackingClassInfo();
  }

  @Override
  public String getName() {
    return "UNPARSEABLE_TYPE_PARAMETER";
  }

  @Override
  public String getSimpleName() {
    return getName();
  }

  @Override
  public IModule getModule() {
    return _owner.getModule();
  }

  @Override
  public String getNamespace() {
    return null;  
  }

  @Override
  public IJavaClassType[] getBounds() {
    return new IJavaClassType[] {JavaTypes.OBJECT().getBackingClassInfo()};
  }

  @Override
  public boolean isFunctionTypeVar() {
    return _owner instanceof IJavaClassMethod;
  }
}
