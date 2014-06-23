/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.nodes;

import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.gs.IGosuArrayClass;
import gw.lang.reflect.gs.FragmentInstance;
import gw.lang.ir.IRType;
import gw.lang.ir.SyntheticIRType;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.fragments.GosuFragment;

public class IRTypeFactory {

  public static IRType get(IType type) {
    if (type instanceof IGosuClassInternal || type instanceof IGosuArrayClass) {
      return GosuClassIRType.get(type);
    } else if (type instanceof GosuFragment) {
      return new SyntheticIRType( FragmentInstance.class, type.getName(), type.getRelativeName());
    } else if (type instanceof IJavaType) {
      return JavaClassIRType.get(((IJavaType) type).getBackingClassInfo());
    } else {
      throw new IllegalArgumentException("Cannot create an IRType for " + type.getClass());
    }
  }
}
