/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.testloader;

import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;

import java.util.Set;
import java.util.HashSet;

public class WrappingTestType extends WrappingTestTypeBase implements IWrappingTestType {

  public WrappingTestType(ITypeLoader loader, String name, IType wrappedType, Class... wrappedInterfaces) {
    init(loader, name, wrappedType, wrappedInterfaces);
  }

  @Override
  protected Set<IType> makeTypeHierarchy() {
    HashSet<IType> types = new HashSet<IType>();
    types.add(this);
    for (Class wrappedInterface : getWrappedInterfaces()) {
      types.add(TypeSystem.get(wrappedInterface));
    }
    return types;
  }

  @Override
  protected ITypeInfo makeTypeInfo() {
    return new WrappingTestTypeInfo(this, getWrappedType());
  }

}
