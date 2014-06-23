/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassWildcardType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.java.asm.AsmWildcardType;
import gw.lang.reflect.module.IModule;

public class AsmWildcardTypeJavaClassWildcardType extends AsmTypeJavaClassType implements IJavaClassWildcardType {
  public AsmWildcardTypeJavaClassWildcardType( AsmWildcardType wildcardType, IModule module ) {
    super( wildcardType, module );
  }

  @Override
  public IJavaClassType getUpperBound() {
    // we only support one bound in Gosu

    AsmType bound = ((AsmWildcardType)getType()).getBound();
    if( bound == null ) {
      return JavaTypes.OBJECT().getBackingClassInfo();
    }
    return AsmTypeJavaClassType.createType( bound, _module );
  }

  @Override
  public IJavaClassType getConcreteType() {
    return getUpperBound();
  }

  @Override
  public String getSimpleName() {
    return getType().getSimpleName();
  }

  @Override
  public IModule getModule() {
    return _module;
  }
}
