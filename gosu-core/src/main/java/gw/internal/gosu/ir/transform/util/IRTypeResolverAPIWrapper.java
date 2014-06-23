/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.util;

import gw.lang.ir.IRType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;


public class IRTypeResolverAPIWrapper implements gw.lang.ir.IRTypeResolver {

  public static final IRTypeResolverAPIWrapper INSTANCE = new IRTypeResolverAPIWrapper(); 

  private IRTypeResolverAPIWrapper() {}

  @Override
  public IRType getDescriptor(IType type) {
    return IRTypeResolver.getDescriptor(type);
  }

  @Override
  public IRType getDescriptor(Class cls) {
    return IRTypeResolver.getDescriptor(cls);
  }

  @Override
  public IRType getDescriptor(IJavaClassInfo cls) {
    return IRTypeResolver.getDescriptor(cls);
  }
}
