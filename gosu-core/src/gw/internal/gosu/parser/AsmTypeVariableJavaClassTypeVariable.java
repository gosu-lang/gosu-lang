/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.java.IJavaClassType;
import gw.lang.reflect.java.IJavaClassTypeVariable;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.asm.AsmType;
import gw.lang.reflect.java.asm.IAsmType;
import gw.lang.reflect.module.IModule;

import java.util.List;

public class AsmTypeVariableJavaClassTypeVariable extends AsmTypeJavaClassType implements IJavaClassTypeVariable {

  public AsmTypeVariableJavaClassTypeVariable( IAsmType typeVariable, IModule module ) {
    super(typeVariable, module);
  }

  @Override
  public IJavaClassType getConcreteType() {
    return getBounds()[0].getConcreteType();
  }

  @Override
  public String getName() {
    return getType().getName();
  }

  @Override
  public String getSimpleName() {
    return getType().getSimpleName();
  }

  @Override
  public IJavaClassType[] getBounds() {
    List<AsmType> typeParameters = getType().getTypeParameters();
    if( typeParameters.isEmpty() ) {
      return new IJavaClassType[] {JavaTypes.OBJECT().getBackingClassInfo()};
    }
    else {
      return new IJavaClassType[] {createType( typeParameters.get( 0 ), getModule() )};
    }
  }

  @Override
  public IModule getModule() {
    return _module;
  }

  public String toString() {
    return getName();
  }
}
