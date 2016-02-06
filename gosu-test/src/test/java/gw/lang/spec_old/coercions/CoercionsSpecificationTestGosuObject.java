/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.spec_old.coercions;

import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

public class CoercionsSpecificationTestGosuObject implements IGosuObject, Runnable {

  public static final Object INSTANCE = new CoercionsSpecificationTestGosuObject();

  public IType getIntrinsicType() {
    return JavaTypes.OBJECT(); // java.lang.Object does not implement Runnable
  }

  public void run() {

  }

}
