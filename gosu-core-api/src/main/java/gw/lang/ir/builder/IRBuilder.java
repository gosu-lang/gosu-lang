/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.ir.builder;

import gw.lang.ir.IRType;
import gw.lang.GosuShop;
import gw.lang.UnstableAPI;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;

import java.util.List;
import java.util.ArrayList;

@UnstableAPI
public abstract class IRBuilder {

  // ------------------------ Protected Helper Methods

  protected final IRType getIRType( Class cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  protected final IRType getIRType( IType type ) {
    return GosuShop.getIRTypeResolver().getDescriptor( type );
  }

  protected final IRType getIRType( IJavaClassInfo cls ) {
    return GosuShop.getIRTypeResolver().getDescriptor( cls );
  }

  protected final List<IRType> getIRTypes( Class[] classes ) {
    List<IRType> results = new ArrayList<IRType>();
    for (Class cls : classes) {
      results.add( getIRType( cls ) );
    }
    return results;
  }
}
